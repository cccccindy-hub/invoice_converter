import {ApiHandle} from "@/tools";
import {TableDefinition} from "@/service/tableDefinition";


export namespace financeService {
    const apiHandle = new ApiHandle("/finance");


    /**
     * 查询 billing 列表
     * @param query 查询参数
     * @returns Promise<{ rows: TableDefinition[]; total: number; }>
     */
    export async function financeAccounts(query: Record<string, any>): Promise<{ rows: TableDefinition[]; total: number }> {
        return await apiHandle.post({url:"/dc_table/accounts",  data: query });
    }

    /**
     * 查询 billing 列表
     * @param query 查询参数
     * @returns Promise<{ rows: TableDefinition[]; total: number; }>
     */
    export async function financeReports(query: Record<string, any>): Promise<{ rows: TableDefinition[]; total: number }> {
        return await apiHandle.post({url:"/dc_table/reports",  data: query });
    }

    /**
     * 查询 billing 列表
     * @param query 查询参数
     * @returns Promise<{ rows: TableDefinition[]; total: number; }>
     */
    export async function financeConfigAccounts(query: Record<string, any>): Promise<{ rows: TableDefinition[]; total: number }> {
        return await apiHandle.post({url:"/dc_table/config/accounts",  data: query });
    }

    /**
     * 查询 billing 列表
     * @param query 查询参数
     * @returns Promise<{ rows: TableDefinition[]; total: number; }>
     */
    export async function financeConfigReports(query: Record<string, any>): Promise<{ rows: TableDefinition[]; total: number }> {
        return await apiHandle.post({url:"/dc_table/config/reports",  data: query });
    }
}