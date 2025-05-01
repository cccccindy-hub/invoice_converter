package com.nnroad.common.utils.excel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.DataFormatData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

public class ExcelCellWriteHandler implements CellWriteHandler {

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        // 3.0 设置单元格为文本
        WriteCellData<?> cellData = context.getFirstCellData();
        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
        DataFormatData dataFormatData = new DataFormatData();
        dataFormatData.setIndex((short) 49);
        writeCellStyle.setDataFormatData(dataFormatData);
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        // 3.0 设置单元格为文本
        Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
        DataFormat dataFormat = workbook.createDataFormat();
        for (WriteCellData<?> writeCellData : cellDataList) {
            WriteCellStyle writeCellStyle = writeCellData.getOrCreateStyle();
            DataFormatData dataFormatData = new DataFormatData();
            dataFormatData.setIndex(dataFormat.getFormat("@"));
            writeCellStyle.setDataFormatData(dataFormatData);
        }
    }
}

