/*
 * Project Name: kmfex-platform
 * File Name: AuthController.java
 * Class Name: AuthController
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hengxin.platform.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.paging.EOperator;
import com.hengxin.platform.common.paging.SearchCriteria;
import com.hengxin.platform.common.paging.SubCriteria;

/**
 * 
 * @author yingchangwang
 * 
 */
public class PaginationUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaginationUtil.class);

    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String OPERATOR = "operator";
    private static final String QUERY_PREFIX = "#";

    public static SearchCriteria buildSearchRequest(HttpServletRequest request) {
        String jsonRequest = request.getParameter("aoData");
        String iDisplayStartStr = "";
        String iDisplayLengthStr = "";
        String sortColumnIndexStr = "";
        String sortDirection = "";
        String sortColumnStr = "";
        String sEcho = "";
        int sortColumnIndex = 0;
        Map<String, SubCriteria> queryMap = new HashMap<String, SubCriteria>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readValue(jsonRequest, JsonNode.class);
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
        }
        if (jsonNode == null) {
            return null;
        }
        for (int i = 0; i < jsonNode.size(); i++) {
            JsonNode obj = jsonNode.get(i);
            if ("sEcho".equals(obj.get(NAME).asText())) {
                sEcho = obj.get(VALUE).asText();
            }
            if ("iDisplayStart".equals(obj.get(NAME).asText())) {
                iDisplayStartStr = obj.get(VALUE).asText();
            }
            if ("iDisplayLength".equals(obj.get(NAME).asText())) {
                iDisplayLengthStr = obj.get(VALUE).asText();
            }
            if ("iSortCol_0".equals(obj.get(NAME).asText())) {
                sortColumnIndexStr = obj.get(VALUE).asText();
            }
            if ("sSortDir_0".equals(obj.get(NAME).asText())) {
                sortDirection = obj.get(VALUE).asText();
            }
            if (!StringUtils.isEmpty(sortColumnIndexStr)) {
                sortColumnIndex = Integer.valueOf(sortColumnIndexStr);
            }
            if (("mDataProp_" + sortColumnIndex).equals(obj.get(NAME).asText())) {
                sortColumnStr = obj.get(VALUE).asText();
            }
            if (obj.get(NAME).asText().startsWith(QUERY_PREFIX)) {
                String value = obj.get(VALUE).asText();
                JsonNode operatorNode = obj.get(OPERATOR);
                String operator = operatorNode == null ? null : obj.get(OPERATOR).asText();
                EOperator o = operator == null ? EOperator.EQ : EnumHelper.translateByLabel(EOperator.class, operator);
                queryMap.put(obj.get(NAME).asText().substring(1), new SubCriteria(o, value));
            }
        }
        Sort sort = null;
        int start = 0;
        int length = 0;
        if (!StringUtils.isEmpty(iDisplayStartStr) && !StringUtils.isEmpty(iDisplayLengthStr)) {
            start = Integer.valueOf(iDisplayStartStr);
            length = Integer.valueOf(iDisplayLengthStr);
        }
        if (!StringUtils.isEmpty(sortColumnStr) && !StringUtils.isEmpty(sortDirection)) {
            sort = new Sort(Direction.fromString(sortDirection), sortColumnStr);
        }
        PageRequest page = new PageRequest(start / length, length, sort);
        SearchCriteria criteria = new SearchCriteria(page);
        criteria.setsEcho(sEcho);
        for (Iterator<Entry<String, SubCriteria>> ite = queryMap.entrySet().iterator(); ite.hasNext();) {
            Entry<String, SubCriteria> type = ite.next();
            criteria.add(type.getKey(), type.getValue().getValue(), type.getValue().getOperator());
        }
        return criteria;
    }

    /**
     * 
     * @param requestDto
     * @return
     */
    public static Pageable buildPageRequest(DataTablesRequestDto requestDto) {

        Sort sort = null;
        List<Integer> sortedColumns = requestDto.getSortedColumns();
        List<String> sortDirections = requestDto.getSortDirections();
        List<String> dataProps = requestDto.getDataProps();

        for (Integer item : sortedColumns) {
            String sortColumn = dataProps.get(item);
            int indexOf = 0;
            if (StringUtils.isNotBlank(sortColumn) && sortColumn.endsWith(".text")) {
                indexOf = sortColumn.lastIndexOf(".text");
            } else if (StringUtils.isNotBlank(sortColumn) && sortColumn.endsWith(".fullText")) {
                indexOf = sortColumn.lastIndexOf(".fullText");
            }
            if (indexOf > 0) {
                sortColumn = sortColumn.substring(0, indexOf);
            }
            String sortDir = sortDirections.get(0);
            sort = new Sort(Direction.fromString(sortDir), sortColumn);
        }

        PageRequest page = new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), sort);
        return page;
    }

    /**
     * Remove suffix and build Sort object
     * 
     * @param requestDto
     * @return
     */
    public static Sort buildSort(DataTablesRequestDto requestDto, String suffix) {
        Sort sort = null;
        List<Integer> sortedColumns = requestDto.getSortedColumns();
        List<String> sortDirections = requestDto.getSortDirections();
        List<String> dataProps = requestDto.getDataProps();

        for (Integer item : sortedColumns) {
            String sortColumn = dataProps.get(item);
            int indexOf = sortColumn.lastIndexOf(suffix);
            if (indexOf > 0) {
                sortColumn = sortColumn.substring(0, indexOf);
            }
            String sortDir = sortDirections.get(0);
            sort = new Sort(Direction.fromString(sortDir), sortColumn);
        }
        return sort;
    }
}
