import request from '@/utils/request'

interface ClientQuery {
  page?: number;
  pageSize?: number;
  [key: string]: any; // to allow other dynamic fields
}

interface ClientData {
  id?: string;
  clientName: string;
  [key: string]: any;
}

// 查询【请填写功能名称】列表
export function listClient(query: ClientQuery): Promise<any> {
  return request({
    url: '/system/client/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】根据供应商ID
export function listClientByVendorId(vendorId: string): Promise<any> {
  return request({
    url: `/system/vendor/${vendorId}/clients`,
    method: 'get',
  })
}

// 查询【请填写功能名称】详细
export function getClient(id: string): Promise<any> {
  return request({
    url: `/system/client/${id}`,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addClient(data: ClientData): Promise<any> {
  return request({
    url: '/system/client',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateClient(data: ClientData): Promise<any> {
  return request({
    url: '/system/client',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delClient(id: string): Promise<any> {
  return request({
    url: `/system/client/${id}`,
    method: 'delete'
  })
}

// deactivate client function
export function deactivateClient(id: string): Promise<any> {
  return request({
    url: `/system/client/${id}/deactivate`,
    method: 'put'
  });
}
