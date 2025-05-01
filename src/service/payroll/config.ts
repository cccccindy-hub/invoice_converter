import { ApiHandle } from "@/tools";

// 定义接口类型
export interface payrollOther {
    id?: number;
    tableName?: string;
    formulaTableName?:string;
    remark?: string;
    [key: string]: any;
}

export namespace payrollConfigService {
    const apiHandle = new ApiHandle("/payroll/config");

    /**
     * 查询 tableDefinition 列表
     * @param query 查询参数
     * @returns Promise<{ rows: payrollOther[]; total: number; }>
     */
    export async function list(query: Record<string, any>): Promise<{ rows: payrollOther[]; total: number }> {
        return await apiHandle.post({url:"/list", data: query });
    }


    /**
     * 查询 tableDefinition 详细信息
     * @param id
     */
    export async function get(id: number): Promise<payrollOther> {
        return await apiHandle.get(`/getInfo/${id}`);
    }



    /**
     * 新增 tableDefinition
     * @param data 表数据
     * @returns Promise<void>
     */
    export async function add(data: payrollOther): Promise<void> {
        return await apiHandle.post({url:"", data: data });
    }

    /**
     * 修改 tableDefinition
     * @param data 表数据
     * @returns Promise<void>
     */
    export async function edit(data: payrollOther): Promise<void> {
        return await apiHandle.post({url:"/edit", data:data });
    }


    /**
     * 删除 tableDefinition
     * @returns Promise<void>
     * @param id
     */
    export async function del(id: number): Promise<void> {
        return await apiHandle.delete(`/${id}`);
    }

    /**
     * 删除 tableDefinition
     * @returns Promise<void>
     * @param data
     */
    export async function View(data: payrollOther): Promise<void> {
        return await apiHandle.post({url:"/view" ,data:data });
    }

}
