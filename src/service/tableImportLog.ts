import { ApiHandle } from "@/tools";

// 定义接口类型
export interface TableImportLog {
    inputFileName?:String;
    [key: string]: any;
}

export namespace TableImportLogService {
    const apiHandle = new ApiHandle("/datacenter/import");

    /**
     * 查询 tableDefinition 列表
     * @param params 查询参数
     * @returns Promise<{ rows: TableImportLog[]; total: number; }>
     */
    export async function list(params: Record<string, any>): Promise<{ rows:any; total: number }> {
        return await apiHandle.post({url:"/list", data: params });
    }

}
