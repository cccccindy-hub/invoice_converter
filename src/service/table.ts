import request from "@/utils/request";

// 定义查询参数类型
interface QueryParams {
  // 添加你的查询参数属性
  name?: string;
  page?: number;
  pageSize?: number;
}

// 定义表格数据类型
interface TableData {
  // 添加你的表格数据属性
  id: number;
  name: string;
  // ...其他属性
}

// 查询【请填写功能名称】列表
export function listTable(query: QueryParams) {
  return request({
    url: "/templateCenter/list",
    method: "get",
    params: query,
  });
}

// 查询【请填写功能名称】详细
export function getTable(formId: number) {
  return request({
    url: `/templateCenter/${formId}`,
    method: "get",
  });
}

// 新增【请填写功能名称】
export function addTable(data: TableData): Promise<any> {
  return request({
    url: "/templateCenter",
    method: "post",
    data: data,
  });
}

// 修改【请填写功能名称】
export function updateTable(data: TableData): Promise<any> {
  return request({
    url: "/templateCenter",
    method: "put",
    data: data,
  });
}

// 删除【请填写功能名称】
export function delTable(formId: number): Promise<any> {
  return request({
    url: `/templateCenter/${formId}`,
    method: "delete",
  });
}
