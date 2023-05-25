<?xml version="1.0"?>
<?mso-application progid="Excel.Sheet"?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
          xmlns:x="urn:schemas-microsoft-com:office:excel"
          xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet">
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
               x:FullRows="1" ss:StyleID="s16" ss:DefaultColumnWidth="54"
               ss:DefaultRowHeight="17.25">
            <Column ss:StyleID="s16" ss:Width="55.5"/>
            <Column ss:StyleID="s16" ss:Width="114"/>
            <Column ss:StyleID="s16" ss:Width="114"/>
            <Column ss:StyleID="s16" ss:AutoFitWidth="0" ss:Width="114"/>
            <Column ss:StyleID="s16" ss:AutoFitWidth="0" ss:Width="114"/>
            <Column ss:StyleID="s16" ss:Width="114"/>
            <Row>
                <Cell><Data ss:Type="String">序号</Data></Cell>
                <Cell><Data ss:Type="String">编码</Data></Cell>
                <Cell><Data ss:Type="String">名称</Data></Cell>
                <Cell><Data ss:Type="String">分类编码</Data></Cell>
                <Cell><Data ss:Type="String">分类名称</Data></Cell>
                <Cell><Data ss:Type="String">备注</Data></Cell>
            </Row>
            <#-- 你需要在[导出文件请求参数实体]ExportFileRequest.data的map类型中指定"aaaDtlList"作为key, 此处模板将会自动完成数据填充 -->
            <#--
                    Map<String,Object> data = new HashMap<>();
                    data.put("aaaDtlList", aaaDtlList);
            -->
            <#-- 注意：当前模板约定只渲染字符串，请手动确保待渲染数据集全属性为字符串 -->
            <#list aaaDtlList as aaaDtl>
                <Row>
                    <Cell ss:StyleID="s17"><Data ss:Type="Number">${aaaDtl_index+1}</Data></Cell>
                    <Cell ss:StyleID="s17"><Data ss:Type="String">${aaaDtl.aaaCode?default("")}</Data></Cell>
                    <Cell ss:StyleID="s17"><Data ss:Type="String">${aaaDtl.aaaName?default("")}</Data></Cell>
                    <Cell ss:StyleID="s17"><Data ss:Type="String">${aaaDtl.aaaTypeCode?default("")}</Data></Cell>
                    <Cell ss:StyleID="s17"><Data ss:Type="String">${aaaDtl.aaaTypeName?default("")}</Data></Cell>
                    <Cell ss:StyleID="s17"><Data ss:Type="String">${aaaDtl.memo?default("")}</Data></Cell>
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
