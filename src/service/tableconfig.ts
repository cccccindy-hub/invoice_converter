import { ApiHandle } from "@/tools";

// 定义接口类型
export interface ColumnConfig {
    columnId?: number;
    tableId?: number;
    columnName?: string;
    columnType?: string;
    columnRemark?: string;
    [key: string]: any;
}

export interface ColumnRoleConfig {
    columnIds: number[]; 
    roleIds: number[];  
  }
  

export namespace tableConfigService {
    const apiHandle = new ApiHandle("/datacenter/config");

    /**
     * 查询 columnConfigTemp 列表
     * @param query 查询参数
     * @returns Promise<{ rows: ColumnConfig[]; total: number; }>
     */
    export async function listTemp(query: Record<string, any>): Promise<{ rows: ColumnConfig[]; total: number }> {
        console.log(query,"查询参数")
        return await apiHandle.post({url:"/listTemp", data: query });
    }

    export async function configBasic() {
        return await apiHandle.get("/basic");
    }

    /**
     * 查询 columnConfigTemp 详细
     * @param columnId 列 ID
     * @returns Promise<ColumnConfig>
     */
    export async function get(columnId: number): Promise<ColumnConfig> {
        return await apiHandle.get(`/${columnId}`);
    }

    /**
     * 新增 columnConfigTemp
     * @param data 列数据
     * @returns Promise<void>
     */
    export async function add(data: ColumnConfig): Promise<void> {
        console.log(data,"ColumnConfig的Data")
        return await apiHandle.post({url:"/addTemp", data: data });
    }

    /**
     * 修改 columnConfigTemp
     * @param data 列数据
     * @returns Promise<void>
     */
    export async function update(data: ColumnConfig): Promise<void> {
        return await apiHandle.post({url:"/editTemp", data: data });
    }

    /**
     * 删除 columnConfigTemp
     * @param columnId 列 ID
     * @param tableId 表 ID
     * @returns Promise<void>
     */
    export async function del(columnId: number, tableId: number): Promise<void> {
        return await apiHandle.get(`/removeTemp/${tableId}/${columnId}`);
    }

    
    export async function getRoles(tableId: number): Promise<ColumnConfig> {
        return await apiHandle.get(`getRoles/${tableId}`);
    }

    export async function assignRoles(data: ColumnRoleConfig): Promise<ColumnConfig> {
        return await apiHandle.post({url:`/authRoleColumn/`, data:data});
    }
}
