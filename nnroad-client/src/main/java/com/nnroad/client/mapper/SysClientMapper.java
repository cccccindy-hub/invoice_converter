package com.nnroad.client.mapper;

import java.util.List;

import com.nnroad.client.domain.SysClient;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * Mapper interface for SysClient operations.
 * 
 * @author nick
 * @date 2024-10-10
 */
public interface SysClientMapper 
{
    /**
     * Query client by Id
     * 
     * @param id The unique identifier of the client.
     * @return The SysClient object associated with the given id.
     */
    public SysClient selectSysClientById(Long id);

    /**
     * Query the list of clients
     * 
     * @param sysClient The criteria for querying clients.
     * @return A list of SysClient objects matching the criteria.
     */
    public List<SysClient> selectSysClientList(SysClient sysClient);

    /**
     * Add a new client
     * 
     * @param sysClient The SysClient object to be added
     * @return the number of rows affected
     */
    public int insertSysClient(SysClient sysClient);

    /**
     * Update a client
     * 
     * @param The SysClient object containing updated data
     * @return the number of rows affected
     */
    public int updateSysClient(SysClient sysClient);

    /**
     * Delete a client by Id
     * 
     * @param id The unique identifier of the client to be deleted.
     * @return the number of rows affected
     */
    public int deleteSysClientById(Long id);

    /**
     * Batch delete clients
     * 
     * @param  array of unique identifiers for the clients to be deleted.
     * @return the number of rows affected
     */
    public int deleteSysClientByIds(Long[] ids);

    String getMaxCodeClient(@Param("prefix") String prefix);

    Integer checkMaxCodeClient(@Param("clientCode") String clientCode,
                               @Param("prefix") String prefix);
    
    /**
     * Get clients by vendor id
     * 
     * @param vendorId Vendor primary key
     * @return List of client objects
     */
	public List<SysClient> getClientByVendorId(Long vendorId);

    public int deactivateSysClientById(Long id);

}
