import {ApiHandle} from "@/tools";
import {objectToFormData} from "@/api/system/app";

// 定义接口类型
export interface TableDefinition {
    tableId?: number;
    tableName?: string;
    tableEnName?: string;
    tableRemark?: string;
    tableType?: string;
    tableStatus?: number;

    [key: string]: any;
}

export namespace datacenterService {
    const apiHandle = new ApiHandle("/datacenter/tableDefinition");
    /**
     * 查询 tableDefinition 列表
     * @param query 查询参数
     * @returns Promise<{ rows: TableDefinition[]; total: number; }>
     */
    export async function list(query: Record<string, any>): Promise<{ rows: TableDefinition[]; total: number }> {
        return await apiHandle.post({
            url: "/list",
            data: query,
        });
    }

    export async function assignTableRole(dataForm) {

        return await apiHandle.post({
            url: "/assign_role",
            data: dataForm
        })
    }

    export async function assignedRoles(tableId) {

        return await apiHandle.get(`/assigned_roles/${tableId}`);
    }

    /**
     * 查询 billing 列表
     * @param query 查询参数
     * @returns Promise<{ rows: TableDefinition[]; total: number; }>
     */
    export async function billingList(query: Record<string, any>): Promise<{ rows: TableDefinition[]; total: number }> {
        return await apiHandle.post({url: "/listAuth", data: query});
    }

    /**
     * 查询 tableDefinition 详细信息
     * @param tableId 表 ID
     * @returns Promise<TableDefinition>
     */
    export async function get(tableId: number): Promise<TableDefinition> {
        return await apiHandle.get(`/getInfo/${tableId}`);
    }

    export async function getTemp(tableId: number): Promise<TableDefinition> {
        return await apiHandle.get(`/getInfoTemp/${tableId}`);
    }


    /**
     * 新增 tableDefinition
     * @param data 表数据
     * @returns Promise<void>
     */
    export async function add(data: TableDefinition): Promise<void> {
        return await apiHandle.post({url: "", data: data});
    }

    /**
     * 修改 tableDefinition
     * @param data 表数据
     * @returns Promise<void>
     */
    export async function edit(data: TableDefinition): Promise<void> {
        return await apiHandle.post({url: "/edit", data: data});
    }


    /**
     * 修改 tableDefinition
     * @param data 表数据
     * @returns Promise<void>
     */
    export async function editTemp(data: TableDefinition): Promise<void> {
        return await apiHandle.post({url: "/editTemp", data: data});
    }


    /**
     * 删除 tableDefinition
     * @param tableId 表 ID
     * @returns Promise<void>
     */
    export async function del(tableId: number): Promise<void> {
        return await apiHandle.delete(`/${tableId}`);
    }

    /**
     * 获取 TableType 选项
     * @returns Promise<any[]>
     */
    export async function fetchTableTypeOptions(): Promise<any[]> {
        return await apiHandle.get("/tableTypeOptions");
    }

    /**
     * 查询 tableDefinition 详情（带临时配置）
     * @param tableId 表 ID
     * @returns Promise<TableDefinition>
     */
    export async function getDetail(tableId: number): Promise<TableDefinition> {
        return await apiHandle.get(`/settingTempTableConfig/${tableId}`);
    }

    /**
     * 同步表数据
     * @param data 同步数据
     * @returns Promise<void>
     */
    export async function synTable(data: { tableId: number; tableSyn: number }): Promise<void> {
        return await apiHandle.post({url: "/synTable", data: data});
    }

    /**
     * 回滚表数据
     * @param data 回滚数据
     * @returns Promise<void>
     */
    export async function rollbackTable(data: { tableId: number; }): Promise<void> {
        return await apiHandle.post({url: "/rollBackTable", data: data});
    }

    /**
     * 创建或变更表
     * @param data 表数据
     * @returns Promise<void>
     */
    export async function createOrChange(data: { tableId: number; [key: string]: any }): Promise<void> {
        return await apiHandle.post({url: "/generateOrChangeTable", data: data});
    }

    /**
     * 停止表操作
     * @param data 表状态数据
     * @returns Promise<void>
     */
    export async function stop(data: { tableId: number; tableStatus: number }): Promise<void> {
        return await apiHandle.post({url: "/stopTable", data: data});
    }
}
