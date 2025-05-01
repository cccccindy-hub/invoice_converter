package com.nnroad.client.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nnroad.client.domain.SysClient;
import com.nnroad.common.utils.NnroadSequence;
import com.nnroad.utils.ExtraAttributeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnroad.client.mapper.SysClientMapper;
import com.nnroad.client.service.ISysClientService;

/**
 * Service implementation for SysClient operations.
 * 
 * @author nick
 * @date 2024-10-10
 */
@Service
public class SysClientServiceImpl implements ISysClientService 
{
    @Autowired
    private SysClientMapper sysClientMapper;
    
    @Autowired
    private ExtraAttributeUtils extraAttributeUtils;

    @Autowired
    private NnroadSequence sequence;



    /**
     * Retrieve a SysClient by its unique identifier.
     * 
     * @param id The unique identifier of the SysClient.
     * @return The SysClient object associated with the given id, or null if not found.
     */
    @Override
    public SysClient selectSysClientById(Long id)
    {
        return sysClientMapper.selectSysClientById(id);
    }

    /**
     * Retrieve a list of SysClient objects.
     * 
     * @param sysClient The criteria for querying clients.
     * @return A list of SysClient objects matching the criteria.
     */
    @Override
    public List<SysClient> selectSysClientList(SysClient sysClient)
    {
        Map<String, Object> processedExtraData = extraAttributeUtils.processExtraDataForSearching(sysClient.getExtraData());
        sysClient.setExtraData(processedExtraData);
        return sysClientMapper.selectSysClientList(sysClient);
    }

    /**
     * Add a new SysClient.
     * 
     * @param sysClient The SysClient object to be added.
     * @return The result of the operation (e.g., number of rows affected).
     */
    @Override
    public int insertSysClient(SysClient sysClient) {
    	if (extraAttributeUtils.validateData(sysClient.getExtraData(), "sys_client")) {
    		return sysClientMapper.insertSysClient(sysClient);
    	}
    	else {
    		throw new IllegalArgumentException("Invalid input data for extra attributes");
    	}
    }

    /**
     * Update an existing SysClient.
     * 
     * @param sysClient The SysClient object containing updated data.
     * @return The result of the operation (e.g., number of rows affected).
     */
    @Override
    public int updateSysClient(SysClient sysClient)
    
    {
        //if the current state of the table is valid (no invalid attributes are appended) run mapper function :Benson
    	if (extraAttributeUtils.validateData(sysClient.getExtraData(), "sys_client")) {
    		return sysClientMapper.updateSysClient(sysClient);
    	}
    	else {
    		throw new IllegalArgumentException("Invalid input data for extra attributes.");
    	}

    }

    /**
     * Batch delete SysClient objects.
     * 
     * @param ids An array of unique identifiers for the SysClients to be deleted.
     * @return The result of the operation (e.g., number of rows affected).
     */
    @Override
    public int deleteSysClientByIds(Long[] ids)
    {
        return sysClientMapper.deleteSysClientByIds(ids);
    }

    /**
     * Delete a SysClient by its unique identifier.
     * 
     * @param id The unique identifier of the SysClient to be deleted.
     * @return The result of the operation (e.g., number of rows affected).
     */
    @Override
    public int deleteSysClientById(Long id)
    {
        return sysClientMapper.deleteSysClientById(id);
    }

	@Override
	public List<SysClient> getClientByVendorId(Long vendorId) {
		return sysClientMapper.getClientByVendorId(vendorId);
		
	}

    @Override
    public boolean checkCode(SysClient client, String prefix) {
        boolean ret = true;

        int cnt = countClientBranches(client, prefix);
        if (cnt > 0) {
            ret = false;
        }

        return ret;
    }

    @Override
    public String resetAndGetCode(String prefix) {
        String maxCode = sysClientMapper.getMaxCodeClient(prefix);

        String tempCode = prefix + "-" + maxCode.replace(prefix, "");

        return sequence.resetAndGetCode(tempCode, "-");
    }

    public Integer countClientBranches(SysClient client, String prefix) {

        return sysClientMapper.checkMaxCodeClient(client.getCompanyCode(), prefix);
    }

    @Override
    public int deactivateSysClientById(Long id) {
        return sysClientMapper.deactivateSysClientById(id);
    }

}
