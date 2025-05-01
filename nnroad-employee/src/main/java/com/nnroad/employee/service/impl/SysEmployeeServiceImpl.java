package com.nnroad.employee.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.nnroad.common.utils.NnroadSequence;
import com.nnroad.utils.ExtraAttributeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnroad.employee.mapper.SysEmployeeMapper;
import com.nnroad.employee.domain.SysEmployee;
import com.nnroad.employee.service.ISysEmployeeService;


/**
 * Service implementation for managing Employee data
 * 
 * This class handles the business logic for employee operations.
 * 
 * @author nick
 * @date 2024-10-11
 */
@Service
public class SysEmployeeServiceImpl implements ISysEmployeeService
{
    @Autowired
    private SysEmployeeMapper sysEmployeeMapper;
    
    @Autowired
    private ExtraAttributeUtils extraAttributeUtils;

    @Autowired
    private NnroadSequence sequence;

    /**
     * Retrieve an employee by ID
     * 
     * @param id Employee primary key
     * @return Employee object
     */
    @Override
    public SysEmployee selectSysEmployeeById(Long id)
    {
        return sysEmployeeMapper.selectSysEmployeeById(id);
    }

    /**
     * Retrieve a list of employees based on criteria
     * 
     * @param sysEmployee Filter criteria for employee retrieval
     * @return List of Employee objects
     */
    @Override
    public List<SysEmployee> selectSysEmployeeList(SysEmployee sysEmployee)
    {
//        Map<String, Object> processedExtraData = extraAttributeUtils.processExtraDataForSearching(sysEmployee.getExtraData());
//        sysEmployee.setExtraData(processedExtraData);
        return sysEmployeeMapper.selectSysEmployeeList(sysEmployee);
    }

    /**
     * Add a new employee
     * 
     * @param sysEmployee Employee object to be added
     * @return Result of the insertion
     */
    @Override
    public int insertSysEmployee(SysEmployee sysEmployee)
    {
    	if (extraAttributeUtils.validateData(sysEmployee.getExtraData(), "sys_employee")) {
    		return sysEmployeeMapper.insertSysEmployee(sysEmployee);
    	}
    	else {
    		throw new IllegalArgumentException("Invalid input data for extra attributes");
    	}
    }

    /**
     * Update an existing employee's information
     * 
     * @param sysEmployee Employee object with updated values
     * @return Result of the update
     */
    @Override
    public int updateSysEmployee(SysEmployee sysEmployee)
    {
    	if (extraAttributeUtils.validateData(sysEmployee.getExtraData(), "sys_employee")) {
    		return sysEmployeeMapper.updateSysEmployee(sysEmployee);
    	}
    	else {
    		throw new IllegalArgumentException("Invalid input data for extra attributes");
    	}
    }

    /**
     * Batch delete employees by their IDs
     * 
     * @param ids Array of primary keys for employees to be deleted
     * @return Result of the batch deletion
     */
    @Override
    public int deleteSysEmployeeByIds(Long[] ids)
    {
        return sysEmployeeMapper.deleteSysEmployeeByIds(ids);
    }

    /**
     * Delete an employee by ID
     * 
     * @param id Employee primary key
     * @return Result of the deletion
     */
    @Override
    public int deleteSysEmployeeById(Long id)
    {
        return sysEmployeeMapper.deleteSysEmployeeById(id);
    }
    
    
    /**
     * Get list of employees by client id
     * 
     * @param id Client primary key
     * @return List of Employee objects
     */
	@Override
	public List<SysEmployee> getEmployeeByClientId(Long clientId) {
		return sysEmployeeMapper.getEmployeesByClientId(clientId);
	}

	 /**
     * Get list of employees by vendor id
     * 
     * @param id Vendor primary key
     * @return List of Employee objects
     */
	@Override
	public List<SysEmployee> getEmployeeByVendorId(Long vendorId) {
		return sysEmployeeMapper.getEmployeesByVendorId(vendorId);
	}

	 /**
     * Off board and employee of a client by employee id
     * 
     * @param clientId Client primary key, employeeId Employee primary key
     * 
     */
	@Override
	public void removeEmployeeFromClient(Long clientId, Long employeeId) {
		sysEmployeeMapper.removeEmployeeFromClient(clientId, employeeId);
	}
	
	 /**
     * Off board and employee of a vendor by employee id
     * 
     * @param clientId Client primary key, employeeId Employee primary key
     * @return Result of the deletion
     * 
     */
	@Override
	public int removeEmployeeFromVendor(Long vendorId, Long employeeId) {
		return sysEmployeeMapper.removeEmployeeFromVendor(vendorId, employeeId);
	}

    @Override
    public boolean checkCode(SysEmployee employee, String prefix) {
        boolean ret = true;
        Map<String, Object> params = new HashMap<>();
        params.put("keyPrefix", prefix);
        employee.setParams(params);
        int cnt = sysEmployeeMapper.checkMaxCodeEmployee(employee);
        if (cnt >0){
            ret = false;
        }

        return ret;
    }

    @Override
    public String resetAndGetCode(String joiner, String prefix) {
        String maxCode = sysEmployeeMapper.getMaxCodeEmployee(prefix);
        return sequence.resetAndGetCode(maxCode, joiner);
    }

    @Override
    public int deactivateEmployeeById(long ids) {
        return sysEmployeeMapper.deactivateEmployeeById(ids);
    }
}
