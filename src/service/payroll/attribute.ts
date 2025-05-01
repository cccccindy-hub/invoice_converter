import { ApiHandle } from "@/tools";

// 定义接口类型
export interface payrollAttribute {
    id?: number;
    baseTableId?:number;
    otherTableId?:number;
    tableName?: string;
    fieldParentId?:string;
    fieldName?:string;
    fieldFormula?:string;
    sortOrder?:string;
    header?:string;
    remark?: string;
    [key: string]: any;
}

export namespace payrollAttributeService {
    const apiHandle = new ApiHandle("/payroll/attribute");

    /**
     * 查询 tableDefinition 列表
     * @param query 查询参数
     * @returns Promise<{ rows: payrollOther[]; total: number; }>
     */
    export async function list(query: Record<string, any>): Promise<{any}> {
        return await apiHandle.post({url:"/list", data: query });
    }


    /**
     * 查询 tableDefinition 详细信息
     * @returns Promise<payrollOther>
     * @param id
     */
    export async function get(id: number): Promise<any> {
        return await apiHandle.get(`/${id}`);
    }


    /**
     * 新增 field
     * @returns Promise<void>
     * @param query
     */
    export async function add(query: Record<string, any>): Promise<void> {
        return await apiHandle.post({url:"/add", data: query });
    }


    /**
     * 修改 tableDefinition
     * @returns Promise<void>
     * @param query
     */
    export async function edit(query: Record<string, any>): Promise<void> {
        return await apiHandle.post({url:"/edit", data:query });
    }

    /**
     * 删除 tableDefinition
     * @returns Promise<void>
     * @param id
     */
    export async function del(id: number): Promise<void> {
        return await apiHandle.delete(`/${id}`);
    }



}
