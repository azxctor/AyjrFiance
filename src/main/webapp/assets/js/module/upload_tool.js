/*
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
define(['jquery',
        'global',
        'requirejs/text!template/upload_tool.templ',
        'module/util',
        'underscore',
        'jquery.ui',
        'module/popup',
        'jquery.iframe-transport',
        'jquery.fileupload'],
    function($,global,template,util){
        /**
         * 构造函数
         * @param options(object) 构造参数，格式{url:xxx}
         * @param element 当前元素
         */
        var UploadTool = function(options,element){
            this.options=$.extend(true,{},UploadTool.defaults,options);
            this.currentElement=element;
            this.init(this.options);
        };
        /**
         * UploadTool 参数定义和方法定义
         */
        $.extend(UploadTool,{
            defaults:{
                url:null,	//必要参数，文件上传地址
                element:null,//必要参数，当前元素
                accept:[],//["image/jpeg","image/png","image/gif"],//默认文件上传类型
                acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
                maxFileSize: 2048000,
                title:"浏览..."
            },
            prototype:{
                /**
                 * 初始化函数
                 * @param options
                 * */
                init:function(options){
                    var $context=$(this.currentElement);
                    var maxFileSize = 2110000;
                    var uploadContainer =_.template(template, {
                        loadingImg:global.context+'/assets/img/loading.gif',
                        closeImg:global.context+"/assets/img/close.png",
                        accept:options.accept.join(","),
                        validateStr:$context.attr("data-validate"),
                        name:$context.attr("name"),
                        title:options.title
                    });
                    var validStr = $context.attr("data-validate");
                    var acceptFile;
                    if(!validStr){
                    	acceptFile = "";
                    }else{
                    	acceptFile = eval('(' + validStr + ')').extension ||  eval('(' + validStr + ')').docextension+"";
                    }

                    var reg_acceptFile = "("+acceptFile.replace(/,/g,"|").replace(/doc/g,"word").replace(/xlsx/g,"spreadsheetml").replace(/xls/g,"excel")+")";
                    options.acceptFileTypes = new RegExp(reg_acceptFile,"i");
                    // fix error binding bug
                    $context.removeAttr("data_error_prop").append(uploadContainer);
                    if($context.attr("pop")){
                        $context.find(".file-binder").val($context.attr("pop"));
                    }
                    $context.find(".upload-title").on("click",function(){
                        $context.find(".upload-input").click();
                    });
                    $context.find(".upload-input").fileupload({
                        url: options.url,
                        dataType: 'json',
                        formData:null,
                        hideDefaultLoading:true,
                        acceptFileTypes: options.acceptFileTypes,
                        /*         maxFileSize: options.maxFileSize,*/
                        add: function(e, data) {
                            $context.find(".upload-input").attr("data-canUp","false");
                            var _$context=$context;
                            var acceptFileTypes = options.acceptFileTypes;
                            if(!acceptFileTypes.test(data.originalFiles[0]['type'])) {
                            	 $.pnotify({
                                     text: "不支持的文件格式,请上传正确的文件格式("+acceptFile+")",
                                     type: 'error'
                                 });
                            }
                            else if(data.originalFiles[0]['size'] > maxFileSize) {
                                _$context.find(".upload-loading").hide();
                                _$context.find(".upload-thumbnail .upload-img").attr("src","");
                                _$context.find(".upload-thumbnail").hide();
                                _$context.find(".upload-filename").empty();
                                _$context.attr("data-img","");
                                $.pnotify({
                                    text: "文件大小必须小于2M，请重新上传",
                                    type: 'error'
                                });
                            }else{
                                data.submit();
                                $context.find(".upload-input").attr("data-canUp","true");
                                $context.find(".upload-input").trigger("change");
                            }
                        },
                        success:function(result){
                            var _$context=$context;
                            if(result.code=="ACK"){
                                var path = result.data.path;
                                if(util.is_pdf(path)) {
                                    _$context.find(".upload-thumbnail .upload-img").attr("src",global.context+"/assets/img/pdf.jpg");
                                    _$context.find(".upload-thumbnail .upload-img").attr("data-pdfpath",path);
                                }
                                else if(util.is_doc(path)){
                                    _$context.find(".upload-thumbnail .upload-img").attr("src",global.context+"/assets/img/doc.jpg");
                                    _$context.find(".upload-thumbnail .upload-img").attr("data-pdfpath",path);
                                }
                                else if(util.is_excel(path)){
                                    _$context.find(".upload-thumbnail .upload-img").attr("src",global.context+"/assets/img/excel.jpg");
                                    _$context.find(".upload-thumbnail .upload-img").attr("data-excelpath",path);
                                }
                                else {
                                    _$context.find(".upload-thumbnail .upload-img").attr("src",path);
                                    _$context.find(".upload-thumbnail .upload-img").removeAttr("data-pdfpath");
                                    _$context.find(".upload-thumbnail .upload-img").removeAttr("data-excelpath");
                                }
                                _$context.find(".upload-thumbnail").show();
                                _$context.attr("data-img",result.data.id);
                                _$context.data("uploaded.file",result.data);
                                _$context.trigger("fileupload.change",path);
                            }
                            else{
                                _$context.find(".upload-thumbnail .upload-img").attr("src","");
                                _$context.find(".upload-thumbnail").hide();
                                _$context.find(".upload-filename").text("");
                                _$context.attr("data-img","");
                            }
                        },
                        progress:function(e,data){
                            $context.find(".upload-loading").show();
                        },
                        fail:function(){
                            var _$context=$context;
                            _$context.find(".upload-thumbnail .upload-img").attr("src","");
                            _$context.find(".upload-thumbnail").hide();
                            _$context.find(".upload-loading").hide();
                            _$context.find(".upload-filename").text("");
                            _$context.attr("data-img","");
                            if($.validator){
                                $context.find(".file-binder").val("").trigger("focusout");
                            }
                        },
                        always:function(){
                            $context.find(".upload-loading").hide();
                        }
                    }).attr('disabled', !$.support.fileInput).parent().addClass($.support.fileInput ? undefined : 'disabled');
                    $context.find(".upload-input").on("change",function(e){
                        if($context.find(".upload-input").attr("data-canUp")!="true") return;
                        if(e.target.files&&e.target.files.length>0){
                            $context.find(".upload-loading").show();
                            $context.find(".upload-filename").text(e.target.files[0].name);
                        }
                        else{
                            $context.find(".upload-loading").hide();
                            $context.find(".upload-filename").text("");
                        }
                        $context.find(".file-binder").val($(this).val());
                        //support for jquery.validator
                        if($.validator){
                            $context.find(".file-binder").trigger("focusout");
                        }
                    });
                    $context.find(".upload-close").on("click",function(){
                        var _$context=$context;
                        _$context.find(".upload-thumbnail .upload-img").attr("src","");
                        _$context.find(".upload-thumbnail").hide();
                        _$context.find(".upload-loading").hide();
                        _$context.find(".upload-filename").text("");
                        _$context.attr("data-img","");
                        if($.validator){
                            $context.find(".file-binder").val("").trigger("focusout");
                        }
                        $context.trigger("fileupload.clear");
                    });
                    if($context.attr("data-disabled") == "true"){
                        $context.css("background-color","#f2f2f2").find(".upload-input").attr("disabled","disabled");
                    }
                },
                /**
                 * 析构函数 还原dom对象
                 */
                destory:function(){
                    var $context=$(this.currentElement);
                    $context.find("*").each(function(){
                        $(this).unbind();
                    });
                    $context.unbind().empty();
                    return this;
                }
            }
        });

        $.extend($.fn,{
            /**
             * 把元素渲染成文件上传控件/调用已渲染的文件上传控件的方法
             * @param options(object/string) 构造参数或者方法
             */
            fileUploader:function(options){
                var $this=$(this);
                if($this.length==0){
                    if(options&&options.debug&&window.console){
                        console.warn("Nothing selected, element not exist.");
                    }
                }
                else{
                    if(typeof options==="object"){
                        $this.each(function(){
                            var uploader=$.data(this,"upload_tool");
                            if(!uploader){
                                uploader=new UploadTool(options,this);
                                $.data(this,"upload_tool",uploader);
                            }
                        });
                    }
                    else if(typeof options==="string"){
                        try{
                            var uploader=$.data(this[0],"upload_tool");
                            if(uploader){
                                options.call(uploader);
                            }
                        }
                        catch(e){
                            if(options&&options.debug&&window.console){
                                console.warn(e.message);
                            }
                        }
                    }
                }
            }
        });
    });