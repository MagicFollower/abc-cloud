<?xml version="1.0"?>
<?mso-application progid="Excel.Sheet"?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
          xmlns:x="urn:schemas-microsoft-com:office:excel"
          xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
>
 <DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
  <Author>微软</Author>
  <LastAuthor>微软</LastAuthor>
  <Created>2021-04-22T14:26:12Z</Created>
  <LastSaved>2021-04-25T09:05:36Z</LastSaved>
  <Company>微软中国</Company>
  <Version>14.00</Version>
 </DocumentProperties>
 <OfficeDocumentSettings xmlns="urn:schemas-microsoft-com:office:office">
  <AllowPNG/>
 </OfficeDocumentSettings>
 <ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
  <WindowHeight>12570</WindowHeight>
  <WindowWidth>28035</WindowWidth>
  <WindowTopX>360</WindowTopX>
  <WindowTopY>60</WindowTopY>
  <ProtectStructure>False</ProtectStructure>
  <ProtectWindows>False</ProtectWindows>
 </ExcelWorkbook>
 <Styles>
  <Style ss:ID="Default" ss:Name="Normal">
   <Alignment ss:Vertical="Center"/>
   <Borders/>
   <Font ss:FontName="宋体" x:CharSet="134" ss:Size="11" ss:Color="#000000"/>
   <Interior/>
   <NumberFormat/>
   <Protection/>
  </Style>
  <Style ss:ID="s16">
   <Font ss:FontName="微软雅黑" x:CharSet="134" x:Family="Swiss" ss:Size="12"
    ss:Color="#000000"/>
  </Style>
  <Style ss:ID="s17">
   <Alignment ss:Horizontal="Left" ss:Vertical="Center"/>
   <Font ss:FontName="微软雅黑" x:CharSet="134" x:Family="Swiss" ss:Size="12"
    ss:Color="#000000"/>
  </Style>
  <Style ss:ID="s18">
   <Alignment ss:Horizontal="Left" ss:Vertical="Center"/>
   <Font ss:FontName="微软雅黑" x:CharSet="134" x:Family="Swiss" ss:Size="12"
    ss:Color="#000000"/>
   <NumberFormat ss:Format="yyyy\-mm\-dd\ hh:mm:ss"/>
  </Style>
  <Style ss:ID="s68">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Font ss:FontName="微软雅黑" x:CharSet="134" x:Family="Swiss" ss:Size="16"
    ss:Color="#000000"/>
  </Style>
 </Styles>
 <Worksheet ss:Name="Sheet1">
  <Table x:FullColumns="1"
   x:FullRows="1" ss:StyleID="s16" ss:DefaultColumnWidth="120"
   ss:DefaultRowHeight="17.25">
   <Column ss:StyleID="s16" ss:Width="55"/>
   <Row>
    <Cell><Data ss:Type="String">序号</Data></Cell>
    <#if RwmStockLtColumns?seq_contains("WAREHOUSENAME")>
        <Cell><Data ss:Type="String">仓库</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("LOCATIONNAME")>
        <Cell><Data ss:Type="String">库位</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVBARCODE")>
        <Cell><Data ss:Type="String">条码</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("STOCKQTY")>
        <Cell><Data ss:Type="String">库存数量</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVCLASSNAME")>
        <Cell><Data ss:Type="String">物料分类</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVCODE")>
        <Cell><Data ss:Type="String">物料编码</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVNAME")>
        <Cell><Data ss:Type="String">物料名称</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVPNO")>
        <Cell><Data ss:Type="String">物料品号</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("COLORNAME")>
        <Cell><Data ss:Type="String">物料颜色</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVMODEL")>
        <Cell><Data ss:Type="String">规格型号</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVCOMPARE")>
        <Cell><Data ss:Type="String">物料成分</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVUNITNAME")>
        <Cell><Data ss:Type="String">计量单位</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVATTRDESC")>
        <Cell><Data ss:Type="String">物料属性描述</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVFROMNAME")>
        <Cell><Data ss:Type="String">物料来源</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVUSENO")>
        <Cell><Data ss:Type="String">用途号</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVBATCHNO")>
        <Cell><Data ss:Type="String">批次</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVFREE")>
        <Cell><Data ss:Type="String">自定义项</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("MEMO")>
        <Cell><Data ss:Type="String">备注</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("CREATEUSER")>
        <Cell><Data ss:Type="String">创建人</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("CREATETIME")>
        <Cell><Data ss:Type="String">创建时间</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("UPDATEUSER")>
        <Cell><Data ss:Type="String">最后编辑人</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("UPDATETIME")>
        <Cell><Data ss:Type="String">最后编辑时间</Data></Cell>
    </#if>
   </Row>
   <#list RwmStockLtDTOs as dto>
   <Row>
    <Cell ss:StyleID="s17"><Data ss:Type="Number">${dto_index+1}</Data></Cell>
    <#if RwmStockLtColumns?seq_contains("WAREHOUSENAME")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.warehouseName!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("LOCATIONNAME")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.locationName!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVBARCODE")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.invBarcode!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("STOCKQTY")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.stockQty!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVCLASSNAME")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.invclassName!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVCODE")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.invCode!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVNAME")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.invName!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVPNO")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.invPno!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("COLORNAME")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.colorName!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVMODEL")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.invModel!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVCOMPARE")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.invCompare!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVUNITNAME")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.invUnitName!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVATTRDESC")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.invAttrDesc!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVFROMNAME")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.invFromName!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVUSENO")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.invUseNo!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVBATCHNO")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.invBatchNo!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("INVFREE")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.invFree!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("MEMO")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.memo!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("CREATEUSER")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.createUser!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("CREATETIME")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.createTime!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("UPDATEUSER")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.updateUser!""}</Data></Cell>
    </#if>
    <#if RwmStockLtColumns?seq_contains("UPDATETIME")>
     <Cell ss:StyleID="s17"><Data ss:Type="String">${dto.updateTime!""}</Data></Cell>
    </#if>
   </Row>
   </#list>
  </Table>
  <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
   <PageSetup>
    <Header x:Margin="0.3"/>
    <Footer x:Margin="0.3"/>
    <PageMargins x:Bottom="0.75" x:Left="0.7" x:Right="0.7" x:Top="0.75"/>
   </PageSetup>
   <Print>
    <ValidPrinterInfo/>
    <PaperSizeIndex>9</PaperSizeIndex>
    <HorizontalResolution>600</HorizontalResolution>
    <VerticalResolution>600</VerticalResolution>
   </Print>
   <Selected/>
   <Panes>
    <Pane>
     <Number>3</Number>
     <ActiveRow>9</ActiveRow>
     <ActiveCol>1</ActiveCol>
    </Pane>
   </Panes>
   <ProtectObjects>False</ProtectObjects>
   <ProtectScenarios>False</ProtectScenarios>
  </WorksheetOptions>
 </Worksheet>
 <Worksheet ss:Name="Sheet2">
  <Table x:FullColumns="1"
   x:FullRows="1" ss:DefaultColumnWidth="54" ss:DefaultRowHeight="13.5">
  </Table>
  <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
   <PageSetup>
    <Header x:Margin="0.3"/>
    <Footer x:Margin="0.3"/>
    <PageMargins x:Bottom="0.75" x:Left="0.7" x:Right="0.7" x:Top="0.75"/>
   </PageSetup>
   <ProtectObjects>False</ProtectObjects>
   <ProtectScenarios>False</ProtectScenarios>
  </WorksheetOptions>
 </Worksheet>
 <Worksheet ss:Name="Sheet3">
  <Table x:FullColumns="1"
   x:FullRows="1" ss:DefaultColumnWidth="54" ss:DefaultRowHeight="13.5">
  </Table>
  <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
   <PageSetup>
    <Header x:Margin="0.3"/>
    <Footer x:Margin="0.3"/>
    <PageMargins x:Bottom="0.75" x:Left="0.7" x:Right="0.7" x:Top="0.75"/>
   </PageSetup>
   <ProtectObjects>False</ProtectObjects>
   <ProtectScenarios>False</ProtectScenarios>
  </WorksheetOptions>
 </Worksheet>
</Workbook>
