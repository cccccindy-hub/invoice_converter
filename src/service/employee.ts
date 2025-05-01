import request from '@/utils/request';

// Define types for the parameters and data
interface EmployeeQuery {
  page?: number;
  pageSize?: number;
  [key: string]: any; // This allows dynamic fields, modify based on actual query fields
}

interface EmployeeData {
  id?: string;
  employeeName: string;
  employeeEmail: string;
  employeePhone: string;
  clientId: number;
  vendorId: number;
  [key: string]: any; // This allows dynamic fields, modify based on actual query fields
  // other fields that might be required for the employee
}

// 查询【员工】列表
export function listEmployee(query: EmployeeQuery): Promise<any> {
  return request({
    url: '/system/employee/list',
    method: 'get',
    params: query,
  });
}

// 查询【客户】下的员工列表
export function listEmployeesByClientId(clientId: string): Promise<any> {
  return request({
    url: `/system/client/${clientId}/employees`,
    method: 'get',
  });
}

// 查询【员工】详细信息
export function getEmployee(id: string): Promise<any> {
  return request({
    url: `/system/employee/${id}`,
    method: 'get',
  });
}

// 新增【员工】
export function addEmployee(data: EmployeeData): Promise<any> {
  return request({
    url: '/system/employee',
    method: 'post',
    data: data,
  });
}

// 修改【员工】
export function updateEmployee(data: EmployeeData): Promise<any> {
  return request({
    url: '/system/employee',
    method: 'put',
    data: data,
  });
}

// 删除【员工】
export function delEmployee(id: string): Promise<any> {
  return request({
    url: `/system/employee/${id}`,
    method: 'delete',
  });
}

export function deactivateEmployee(id: string): Promise<any> {
  return request({
    url: `/system/employee/${id}/deactivate`,
    method: 'put'
  });
}
