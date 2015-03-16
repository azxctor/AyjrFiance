
/*
* Project Name: kmfex-platform
* File Name: ExcelCellsTypeRecognizer.java
* Class Name: ExcelCellsTypeRecognizer
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
	
package com.hengxin.platform.common.domain;


/**
 * Class Name: ExcelCellsTypeRecognizer
 * Description: TODO
 * @author tingwang
 *
 */

public class ExcelCellsTypeRecognizer {
    
    int[] cellsType;
    
    public ExcelCellsTypeRecognizer() {
        super();
    }
    	
    public ExcelCellsTypeRecognizer(int[] cellsType) {
        super();
        this.cellsType = cellsType;
    }

    public int[] getCellsType() {
        return cellsType;
    }

    public void setCellsType(int[] cellsType) {
        this.cellsType = cellsType;
    }

}
