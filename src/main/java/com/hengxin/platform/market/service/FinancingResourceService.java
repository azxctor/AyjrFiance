/*
 * Project Name: kmfex-platform
 * File Name: FinancingResourceService.java
 * Class Name: FinancingResourceService
 *
 * Copyright 2014 KMFEX Inc
 *
 * 
 *
 * http://www.kmfex.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.market.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hengxin.platform.common.util.resource.ResourceManager;
import com.hengxin.platform.common.util.resource.ResourcePool;
import com.hengxin.platform.market.dto.MarketItemDto;
import com.hengxin.platform.product.enums.EPackageStatus;

/**
 * Class Name: FinancingResourceService
 * 
 * @author runchen
 * 
 */
@Service
public class FinancingResourceService implements ResourceManager<MarketItemDto> {

	private static final String CACHE_MAP = "marketCache";
	private static final Logger LOGGER = LoggerFactory.getLogger(FinancingResourceService.class);

	@Autowired
	private ResourcePool resource;

	@Autowired
	private transient HazelcastInstance inst;

	@Override
	public long consume(String key, long count) {
		return resource.consume(key, count);
	}

	@Override
	public long get(String res) {
		return resource.get(res);
	}

	@Override
	public void offer(String key, long count) {
		resource.offer(key, count);
	}

	private IMap<String, Object> getMap() {
		return inst.getMap(CACHE_MAP);
	}

	@Override
	public void removeItem(final String key) {
		removeItem(key, false);
	}

	@Override
	public void removeItem(final String key, boolean deep) {
		if (deep) {
			resource.remove(key);
			getMap().remove(key);
		} else {
			MarketItemDto item = getItem(key);
			if (item != null) {
				item.setExpiredTime(System.currentTimeMillis());
				item.setStatus(EPackageStatus.WAIT_SIGN);
				addItem(item, false);
			}
		}
	}

	@Override
	public void addItem(MarketItemDto item, boolean deep) {
		BigDecimal subtract = item.getPackageQuota().subtract(
				item.getSupplyAmount());
		if (item.getProgress() == null) {
			MarketItemDto existing = getItem(item.getId());
			item.setProgress(existing != null ? existing.getProgress() : item
					.getSupplyAmount().divide(item.getPackageQuota(), 3,
							RoundingMode.DOWN));
		}
		getMap().put(item.getId(), item);
		if (deep) {
			LOGGER.info("[RESOURCE CHANGE]addItem: {}, value {}", item.getId(),
					subtract.longValue());
			resource.put(item.getId(), subtract.longValue());
		}
	}

	@Override
	public MarketItemDto getItem(String key) {
		return (MarketItemDto) getMap().get(key);
	}

	public Map<String, MarketItemDto> getAsMap() {
		Map<String, MarketItemDto> map = new HashMap<String, MarketItemDto>();
		for (Object record : getMap().values()) {
			MarketItemDto item = (MarketItemDto) record;
			map.put(item.getId(), item);
		}
		return map;
	}

	public void updateItem(String id, ItemVisitor visitor) {
		MarketItemDto item = getItem(id);
		if (item != null) {
			visitor.visit(item);
			addItem(item, false);
		}
	}

	public List<MarketItemDto> getAsList() {
		List<MarketItemDto> list = new ArrayList<MarketItemDto>();
		for (Object record : getMap().values()) {
			MarketItemDto item = (MarketItemDto) record;
			list.add(item);
		}
		return list;
	}

	public interface ItemVisitor {

		void visit(MarketItemDto item);

	}

}
