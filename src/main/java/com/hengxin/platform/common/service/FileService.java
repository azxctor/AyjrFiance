/*
 * File Name: SysDictService.java
 * Project Name: kmfex-platform
 * Class Name: SysDictService
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

package com.hengxin.platform.common.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.SysFile;
import com.hengxin.platform.common.entity.SysFilePo;
import com.hengxin.platform.common.repository.FileRepository;
import com.hengxin.platform.common.util.FileUploadUtil;

/**
 * Class Name: SysDictService
 * 
 * @author runchen
 * 
 */
@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Transactional
    public void uploadAndSaveFile(InputStream inputStream, File file, SysFile sysFile) throws IOException {
        FileUtils.copyInputStreamToFile(inputStream, file);
        sysFile.setCreatedTs(new Date());
        SysFilePo filePo = ConverterService.convert(sysFile, SysFilePo.class);
        fileRepository.save(filePo);
    }

    @Transactional
    public void removeFile(String fileId) throws IOException {
        SysFilePo filePo = fileRepository.findOne(fileId);
        if (filePo == null) {
            return;
        }
        fileRepository.delete(filePo);
        removePhysicalFile(filePo.getId());
    }
    
    /**
     * 删除物理文件
     * @param fileId
     */
    private void removePhysicalFile(String fileId){
        File file = new File(FileUploadUtil.getFolder(), fileId);
        try {
			FileUtils.forceDeleteOnExit(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
