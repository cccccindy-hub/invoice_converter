import {ApiHandle} from "@/tools";
import Request from "@/utils/request";

// 定义接口类型
export interface TableData {
    id?: number;
    tableId?: number;
    [key: string]: any;
}

export namespace InvoiceService {
    const apiHandle = new ApiHandle("/invoice");

    /**
     * 查询表格配置数据
     * @param tableId 表 ID
     * @returns Promise<any>
     */
    export async function getTableConfigData(tableId: number): Promise<any> {
        return await apiHandle.get(`/showTable/${tableId}`);
    }

    /**
     * 获取表格数据
     * @param params 查询参数
     * @returns Promise<TableData[]>
     */
    export async function getTableData(params: Record<string, any>): Promise<{ rows: any[]; total: number }> {
        console.log("Sending request to /datacenter/tableResult/list with params:", params); // 添加日志
        return await apiHandle.post({url:"/list", data:params}); // 将查询参数作为请求体传递
    }



    /**
     * 新增表格数据
     * @param data 新增数据
     * @param tableId 表 ID
     * @returns Promise<void>
     */
    export async function addTableData(data: TableData, tableId: number): Promise<void> {
        return await apiHandle.post({url:`/add/${tableId}`, data:data });
    }

    export async function editTableData(data: TableData, tableId: number, id: number) {

        return await apiHandle.post({url:`/edit/${tableId}/${id}`, data: data })
    }

    /**
     * 删除表格数据
     * @param id 数据 ID
     * @param tableId 表 ID
     * @returns Promise<void>
     */
    export async function deleteTableData(id: number, tableId: number): Promise<void> {
        return await apiHandle.delete(`/${tableId}/${id}`);
    }


    export async function importTableData(
        formData: file // 使用 FormData 类型来确保传递的是正确的数据
    ): Promise<any> {
        return await apiHandle.post({
            url: `/import/importData`,
            data: formData,
            headers: {
                "Content-Type": "multipart/form-data",
            },
        });
    }
}
