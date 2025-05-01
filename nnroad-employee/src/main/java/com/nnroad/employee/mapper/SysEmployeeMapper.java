package com.nnroad.employee.mapper;

import java.util.List;

import com.nnroad.employee.domain.SysEmployee;
import org.apache.ibatis.annotations.Param;
/**
 * Mapper interface for Employee
 * 
 * Provides methods to perform CRUD operations on the SysEmployee entity.
 * 
 * @author nick
 * @date 2024-10-11
 */
public interface SysEmployeeMapper 
{
    /**
     * Retrieve an employee by ID
     * 
     * @param id Employee primary key
     * @return Employee object
     */
    public SysEmployee selectSysEmployeeById(Long id);

    /**
     * Retrieve a list of employees
     * 
     * @param sysEmployee Employee filter criteria
     * @return List of Employee objects
     */
    public List<SysEmployee> selectSysEmployeeList(SysEmployee sysEmployee);

    /**
     * Insert a new employee
     * 
     * @param sysEmployee Employee object to be added
     * @return Result of the insertion
     */
    public int insertSysEmployee(SysEmployee sysEmployee);

    /**
     * Update an existing employee
     * 
     * @param sysEmployee Employee object with updated values
     * @return Result of the update
     */
    public int updateSysEmployee(SysEmployee sysEmployee);

    /**
     * Delete an employee by ID
     * 
     * @param id Employee primary key
     * @return Result of the deletion
     */
    public int deleteSysEmployeeById(Long id);

    /**
     * Batch delete employees
     * 
     * @param ids Array of primary keys of employees to be deleted
     * @return Result of the batch deletion
     */
    public int deleteSysEmployeeByIds(Long[] ids);
    
    /**
     * Get employees by client id
     * 
     * @param id Client primary key
     * @return List of employee object
     */
    public List<SysEmployee> getEmployeesByClientId(Long clientId);
    
    /**
     * Get employees by vendor id
     * 
     * @param id vendor primary key
     * @return List of employee objects
     */
	public List<SysEmployee> getEmployeesByVendorId(Long vendorId);

	/**
     * Set client id of a employee to Null
     * 
     * @param clientId Client primary key, employeeId Employee primary key
     * 
     */
	public void removeEmployeeFromClient(@Param("clientId") Long clientId, @Param("employeeId") Long employeeId);
	
	/**
     * Set vendor id of a employee to Null
     * 
     * @param vendorId Vendor primary key, employeeId Employee primary key
     * 
     */
	public int removeEmployeeFromVendor(@Param("vendorId") Long vendorId, @Param("employeeId") Long employeeId);

    public int checkMaxCodeEmployee(SysEmployee employee);

    public String getMaxCodeEmployee(String prefix);

    public int deactivateEmployeeById(long ids);

}
