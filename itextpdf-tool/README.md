# 依赖库名称

封装ITextPdf工具，提供通用生成pdf文件的功能。

## 逻辑说明：

### createDocument 创建文档

返回的文档key须保存，后续用来生成文档/压缩包。

```
 /**
     * 创建pdf文档
     *
     * @param fileName 文件名称（带扩展名）
     * @return 文档key
     * @throws IOException
     * @throws DocumentException
     */
    @NaslLogic
    public String createDocument(String fileName) 
```

### closeDocument 关闭文档

关闭文档后，在服务器本地生成文件，返回文件本地路径。该本地路径跟文件连接器无关。

```
/**
     * 关闭文档
     *
     * @param documentKey
     * @return 文件本地路径
     */
    @NaslLogic
    public String closeDocument(String documentKey) {
  
```

### closeDocumentGetFileURl 关闭文档并且获取文件地址

关闭文档后，在服务器本地生成文件，并且把文件通过默认文件连接器配置上传至文件连接器。最终返回文件连接器中该文件的地址。

```
 /**
     * 关闭文档并且获取文件地址
     *
     * @param documentKey
     * @return 文件访问信息
     */
    @NaslLogic
    public UploadResponseDTO closeDocumentGetFileURl(String documentKey) 

```

### addDocumentElement 组装元素

组装元素，返回组装结果。

```
 /**
     * 组装元素
     *
     * @param addedElementKey 子元素key
     * @param addElementKey   父元素key
     * @return
     * @throws DocumentException
     */
    @NaslLogic
    public Boolean addDocumentElement(String addedElementKey, String addElementKey) {

```

### buildParagraph 构造段落

构造段落，返回段落key。

```
 /**
     * 构造段落
     *
     * @param paragraphStr            段落文本。可空，空时创建空段落
     * @param iTextParagraphStructure 段落配置
     * @param iTextFontStructure      段落文本格式。paragraphStr为空时可空
     * @return 段落key
     * @throws DocumentException
     * @throws IOException
     */
    @NaslLogic
    public String buildParagraph(String paragraphStr, ITextParagraphStructure iTextParagraphStructure, ITextFontStructure iTextFontStructure) 
```

### buildTable 构造表格

构造表格，返回表格key。

```
 /**
     * 构造表格
     *
     * @param iTextTableStructure 表格配置
     * @return
     * @throws DocumentException
     */
    @NaslLogic
    public String buildTable(ITextTableStructure iTextTableStructure) 
```

### buildCell 构造单元格

构造单元格，返回单元格key。

```
/**
 * @param cellStr            单元格文本。可空，空时创建空段落
 * @param iTextCellStructure 单元格配置
 * @param iTextFontStructure 单元格文本格式。paragraphStr为空时可空
 * @return
 * @throws DocumentException
 * @throws IOException
 */
    @NaslLogic
    public String buildCell(String cellStr, ITextCellStructure iTextCellStructure, ITextFontStructure iTextFontStructure) {

```

### buildChunk 构造Chunk

构造Chunk，返回Chunk key。

```
  /**
 * 构造chunk
 *
 * @param chunkStr            文本。可空，空时创建空段落
 * @param iTextChunkStructure 配置
 * @param iTextFontStructure  文本格式
 * @return
 */
    @NaslLogic
    public String buildChunk(String chunkStr, ITextChunkStructure iTextChunkStructure, ITextFontStructure iTextFontStructure) {

```

### buildPhrase 构造短语

构造短语，返回短语key。

```
  /**
 * 构造短语
 * @param phraseStr 文本。可空，空时创建空段落
 * @param iTextFontStructure 文本格式
 * @param iTextPhraseStructure 短语配置
 * @return
 */
    @NaslLogic
    public String buildPhrase(String phraseStr, ITextFontStructure iTextFontStructure, ITextPhraseStructure iTextPhraseStructure) {

```

### buildListItem 构造列表项

构造列表项，返回列表项key。

```
  /**
 * 构造列表
 * @param listItemStr 列表项文本。可空，空时创建空列表
 * @param iTextFontStructure 列表项文本格式
 * @param iTextListStructure 列表配置
 * @return
 */
    @NaslLogic
    public String buildListItem(String listItemStr, ITextFontStructure iTextFontStructure, ITextListStructure iTextListStructure) {

```

### buildImage 构造图片

构造图片，返回图片key。

```
 /**
 * 构造图片元素
 * @param imageUrl 图片url
 * @param iTextImageStructure 图片配置
 * @return
 */
    @NaslLogic
    public String buildImage(String imageUrl, ITextImageStructure iTextImageStructure) {

```

### buildImageBase64 使用base64字符串构造图片

构造图片，返回图片key。

```
/**

* 构造图片元素
*
* @param base64ImageString 图片base64字符串
* @param iTextImageStructure 图片配置
* @return
  */
  @NaslLogic
  public String buildImageBase64(String base64ImageString, ITextImageStructure iTextImageStructure) {
```

### addCanvasImage 新增图片canvas

新增图片canvas，置于顶层。可用于插入盖章等全文档随意位置的图片元素。

```
/**
 * 新增图片canvas，置于顶层
 * @param documentKey
 * @param imageKey
 * @return
 */
    @NaslLogic
    public Boolean addCanvasImage(String documentKey, String imageKey) 
```
### mergePdf 合并pdf
```
 /**
     * 合并pdf
     *
     * @param inputFiles              原文件地址集合
     * @param targetFileName          目标文件名
     * @param isDeleteOriginLocalFile 是否删除本地原文件
     * @return 目标文件本地路径
     */
    @NaslLogic
    public String mergePdf(List<String> inputFiles, String targetFileName, Boolean isDeleteOriginLocalFile) 
```
### 
``` mergePdfToNosFileURl 合并pdf，并上传到nos
 /**
     * 合并pdf，并上传到nos
     *
     * @param inputFiles              原文件地址集合
     * @param targetFileName          目标文件名，文件名称不可重复
     * @param isDeleteOriginLocalFile 是否删除本地原文件
     * @return nos文件访问信息
     */
    @NaslLogic
    public UploadResponseDTO mergePdfToNosFileURl(List<String> inputFiles, String targetFileName, Boolean isDeleteOriginLocalFile) 
```

## 结构说明

### 字体格式

|     字段名      |   类型    | 描述/可选值                                                                                  |
|:------------:|:-------:|:----------------------------------------------------------------------------------------
|   fontName   | String  | 字体名称，默认STSong-Light                                                                     
| fontEncoding | String  | 字体编码，默认UniGB-UCS2-H                                                                     
|   embedded   | Boolean | 是否嵌入字体：true表示将字体文件嵌入文档（确保跨设备显示一致），false表示不嵌入                                            
|  forceRead   | Boolean | 强制读取字体：true表示忽略系统缓存强制重新加载字体文件（解决字体更新问题）                                                 
|     size     | Double  | 字体大小（单位：磅/pt）                                                                           
|    style     | String  | 字体样式：NORMAL/BOLD/ITALIC/UNDERLINE/STRIKETHRU/BOLDITALIC/UNDEFINED/DEFAULTSIZE           
|    color     | String  | 颜色：WHITE/LIGHT_GRAY/GRAY/DARK_GRAY/BLACK/RED/PINK/ORANGE/YELLOW/GREEN/MAGENTA/CYAN/BLUE 

### 单元格配置

| 字段名                      | 	类型       | 	描述/可选值                                                                                                                                           |
|:-------------------------|:----------|:--------------------------------------------------------------------------------------------------------------------------------------------------|
| border                   | 	String	  | 全局边框类型：UNDEFINED/TOP/BOTTOM/LEFT/RIGHT/NO_BORDER/BOX                                                                                              |
| borderWidth              | 	Double	  | 单元格边框宽度                                                                                                                                           |
| padding                  | 	Double	  | 单元格内边距                                                                                                                                            |
| borderColor	             | String	   | 边框颜色：WHITE/LIGHT_GRAY/GRAY/DARK_GRAY/BLACK/RED/PINK/ORANGE/YELLOW/GREEN/MAGENTA/CYAN/BLUE                                                         |
| paddingLeft              | 	Double	  | 左边距                                                                                                                                               |
| paddingRight	            | Double    | 	右边距                                                                                                                                              |
| paddingTop               | 	Double	  | 上边距                                                                                                                                               |
| paddingBottom            | 	Double	  | 下边距                                                                                                                                               |
| horizontalAlignmentText	 | String	   | 水平对齐方式：ALIGN_UNDEFINED/ALIGN_LEFT/ALIGN_CENTER/ALIGN_RIGHT/ALIGN_JUSTIFIED/ALIGN_TOP/ALIGN_MIDDLE/ALIGN_BOTTOM/ALIGN_BASELINE/ALIGN_JUSTIFIED_ALL 
| verticalAlignmentText    | 	String	  | 垂直对齐方式：ALIGN_UNDEFINED/ALIGN_LEFT/ALIGN_CENTER/ALIGN_RIGHT/ALIGN_JUSTIFIED/ALIGN_TOP/ALIGN_MIDDLE/ALIGN_BOTTOM/ALIGN_BASELINE/ALIGN_JUSTIFIED_ALL 
| fixedHeight              | 	Double	  | 固定高度                                                                                                                                              
| minimumHeight	           | Double    | 	最小高度                                                                                                                                             
| fixedLeading	            | Double	   | 行间距（固定值）                                                                                                                                          
| multipliedLeading	       | Double	   | 行间距（倍数）                                                                                                                                           
| backgroundColor	         | String	   | 背景颜色：WHITE/LIGHT_GRAY/GRAY/DARK_GRAY/BLACK/RED/PINK/ORANGE/YELLOW/GREEN/MAGENTA/CYAN/BLUE                                                         
| colSpan                  | 	Integer	 | 横向合并列数                                                                                                                                            
| rowSpan                  | 	Integer	 | 纵向合并行数                                                                                                                                            

### Chunk配置

| 字段名              | 	类型     | 	描述/可选值                                                                                            |
|:-----------------|:--------|:---------------------------------------------------------------------------------------------------|
| characterSpacing | 	Double | 	设置字符间距                                                                                            
| anchor	          | String	 | 添加超链接（可以是URL或文档内锚点）                                                                                
| wordSpacing	     | Double  | 	设置单词间距（仅对空格字符有效）                                                                                  
| backgroundColor	 | String	 | 设置背景色（颜色值或枚举：WHITE/LIGHT_GRAY/GRAY/DARK_GRAY/BLACK/RED/PINK/ORANGE/YELLOW/GREEN/MAGENTA/CYAN/BLUE） 

### 图片配置

| 字段名                  | 	类型       | 	描述/可选值                                                                                                                                         |
|:---------------------|:----------|:------------------------------------------------------------------------------------------------------------------------------------------------|
| scaleToFitHeight	    | Double	   | 按比例缩放图片到目标高度（保持宽高比）                                                                                                                             
| scaleToFitWidth      | 	Double   | 	按比例缩放图片到目标宽度（保持宽高比）                                                                                                                            
| scalePercent         | 	Double	  | 缩放图片的百分比（100表示原尺寸）                                                                                                                              
| scaleAbsoluteWidth   | 	Double   | 	固定宽度，高度按比例调整                                                                                                                                   
| scaleAbsoluteHeight	 | Double    | 	固定高度，宽度按比例调整                                                                                                                                   
| alignText	           | String	   | 对齐方式：ALIGN_UNDEFINED/ALIGN_LEFT/ALIGN_CENTER/ALIGN_RIGHT/ALIGN_JUSTIFIED/ALIGN_TOP/ALIGN_MIDDLE/ALIGN_BOTTOM/ALIGN_BASELINE/ALIGN_JUSTIFIED_ALL 
| absoluteX	           | Double    | 	设置绝对X坐标（相对于页面左下角）                                                                                                                              
| absoluteY	           | Double	   | 设置绝对Y坐标（相对于页面左下角）                                                                                                                               
| indentationLeft      | 	Double   | 	左缩进（需在非绝对定位模式下使用）                                                                                                                              
| border	              | String    | 	全局边框：UNDEFINED/TOP/BOTTOM/LEFT/RIGHT/NO_BORDER/BOX                                                                                             
| borderWidth	         | Double	   | 边框宽度                                                                                                                                            
| borderColor	         | String	   | 边框颜色：WHITE/LIGHT_GRAY/GRAY/DARK_GRAY/BLACK/RED/PINK/ORANGE/YELLOW/GREEN/MAGENTA/CYAN/BLUE                                                       
| spacingBefore        | 	Double	  | 图片上方的空白间距                                                                                                                                       
| spacingAfter         | 	Double	  | 图片下方的空白间距                                                                                                                                       
| transparency         | 	List	    | 透明度（ARGB值），示例：[200, 255, 255, 255]（A=200, R=255, G=255, B=255）                                                                                  
| anchor	              | String    | 	设置超链接（点击跳转URL）                                                                                                                                 
| compressionLevel     | 	Integer  | 	JPEG压缩质量（0-9，0=最低压缩/质量最差，9=最高压缩/质量最好）                                                                                                          
| dpiX	                | Integer	  | 水平DPI（影响打印质量）                                                                                                                                   
| dpiY                 | 	Integer	 | 垂直DPI（影响打印质量）                                                                                                                                   

### 列表配置

| 字段名                | 	类型      | 	描述/可选值                                                                                                                                          |
|:-------------------|:---------|:-------------------------------------------------------------------------------------------------------------------------------------------------|
| alignText	         | String   | 	对齐方式：ALIGN_UNDEFINED/ALIGN_LEFT/ALIGN_CENTER/ALIGN_RIGHT/ALIGN_JUSTIFIED/ALIGN_TOP/ALIGN_MIDDLE/ALIGN_BOTTOM/ALIGN_BASELINE/ALIGN_JUSTIFIED_ALL 
| indentationLeft    | 	Double	 | 左缩进（单位：像素）                                                                                                                                       
| indentationRight   | 	Double	 | 右缩进（单位：像素）                                                                                                                                       
| firstLineIndent    | 	Double	 | 首行缩进（单位：像素）                                                                                                                                      
| keepTogether	      | Boolean	 | 是否禁止分页时拆分段落（true表示整段必须在同一页）                                                                                                                      
| spacingBefore      | 	Double	 | 段前间距（单位：像素）                                                                                                                                      
| spacingAfter	      | Double	  | 段后间距（单位：像素）                                                                                                                                      
| paddingTop	        | Double   | 	上边距（单位：像素）                                                                                                                                      
| fixedLeading       | 	Double	 | 行间距（固定值，单位：像素）                                                                                                                                   
| multipliedLeading	 | Double   | 	行间距（倍数，如1.5表示1.5倍行距）                                                                                                                            

### 段落配置

| 字段名               | 	类型       | 	描述/可选值                                                                                                                                          |
|:------------------|:----------|:-------------------------------------------------------------------------------------------------------------------------------------------------|
| alignText         | 	String   | 	对齐方式：ALIGN_UNDEFINED/ALIGN_LEFT/ALIGN_CENTER/ALIGN_RIGHT/ALIGN_JUSTIFIED/ALIGN_TOP/ALIGN_MIDDLE/ALIGN_BOTTOM/ALIGN_BASELINE/ALIGN_JUSTIFIED_ALL 
| indentationLeft	  | Double	   | 左缩进（单位：像素）                                                                                                                                       
| indentationRight  | 	Double	  | 右缩进（单位：像素）                                                                                                                                       
| firstLineIndent   | 	Double   | 	首行缩进（单位：像素）                                                                                                                                     
| spacingBefore     | 	Double   | 	段前间距（单位：像素）                                                                                                                                     
| spacingAfter      | 	Double   | 	段后间距（单位：像素）                                                                                                                                     
| keepTogether      | 	Boolean	 | 是否禁止分页时拆分段落（true表示整段必须在同一页）                                                                                                                      
| fixedLeading      | 	Double	  | 行间距（固定值，单位：像素）                                                                                                                                   
| multipliedLeading | 	Double   | 	行间距（倍数，如 1.5 表示1.5倍行距）                                                                                                                          

### Phrase短语配置

| 字段名                | 	类型     | 	描述/可选值                        |
|:-------------------|:--------|:-------------------------------|
| fixedLeading       | 	Double | 	行间距（固定值，单位：像素/磅）              
| multipliedLeading	 | Double  | 	行间距（倍数，基于字体大小计算，如1.5表示1.5倍行距） 

### 表格配置

| 字段名               | 	类型       | 	描述/可选值                                                                                                                                           |
|:------------------|:----------|:--------------------------------------------------------------------------------------------------------------------------------------------------|
| alignText         | 	String	  | 文本对齐方式：ALIGN_UNDEFINED/ALIGN_LEFT/ALIGN_CENTER/ALIGN_RIGHT/ALIGN_JUSTIFIED/ALIGN_TOP/ALIGN_MIDDLE/ALIGN_BOTTOM/ALIGN_BASELINE/ALIGN_JUSTIFIED_ALL 
| indentationLeft   | 	Double   | 	左缩进（单位：像素）                                                                                                                                       
| indentationRight	 | Double	   | 右缩进（单位：像素）                                                                                                                                        
| firstLineIndent   | 	Double   | 	首行缩进（单位：像素）                                                                                                                                      
| spacingBefore     | 	Double   | 	段前间距（单位：像素）                                                                                                                                      
| spacingAfter	     | Double	   | 段后间距（单位：像素）                                                                                                                                       
| keepTogether      | 	Boolean	 | 段落分页控制：true-禁止拆分段落（整段必须在同一页）false-允许拆分段落                                                                                                          
| fixedLeading      | 	Double	  | 固定行高（单位：像素）                                                                                                                                       
| multipliedLeading | 	Double   | 	相对行高（基于字体大小的倍数，如1.5=1.5倍行距）                                                                                                                      
