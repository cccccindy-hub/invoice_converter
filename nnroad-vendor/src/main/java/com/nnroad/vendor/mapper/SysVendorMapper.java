package com.nnroad.vendor.mapper;

import java.util.List;

import com.nnroad.vendor.domain.SysVendor;
import org.apache.ibatis.annotations.Param;


/**
 * Mapper interface for handling operations related to the SysVendor entity.
 * 
 * 
 * @author nick
 * @date 2024-10-11
 */
public interface SysVendorMapper 
{
    /**
     * Retrieve a SysVendor by its primary key.
     * 
     * @param id The primary key of the SysVendor
     * @return The SysVendor object corresponding to the provided ID
     */
    public SysVendor selectSysVendorById(Long id);

    /**
     * Retrieve a list of SysVendor objects based on specified criteria.
     * 
     * @param sysVendor An instance of SysVendor containing the search criteria
     * @return A list of SysVendor objects that match the criteria
     */
    public List<SysVendor> selectSysVendorList(SysVendor sysVendor);

    /**
     * Insert a new SysVendor into the database.
     * 
     * @param sysVendor The SysVendor object to be inserted
     * @return The number of rows affected
     */
    public int insertSysVendor(SysVendor sysVendor);

    /**
     * Update an existing SysVendor in the database.
     * 
     * @param sysVendor The SysVendor object containing updated information
     * @return The number of rows affected
     */
    public int updateSysVendor(SysVendor sysVendor);

    /**
     * Delete a SysVendor by its primary key.
     * 
     * @param id The primary key of the SysVendor to be deleted
     * @return The number of rows affected
     */
    public int deleteSysVendorById(Long id);

    /**
     * Batch delete SysVendor records by their primary keys.
     * 
     * @param ids An array of primary keys of the SysVendor records to be deleted
     * @return The number of rows affected
     */
    public int deleteSysVendorByIds(Long[] ids);
    
    /**
     * Get vendors by client id
     * 
     * @param primary key of client
     * @return List of vendor object
     */
	public List<SysVendor> getVendorByClientId(Long clientId);

	public List<SysVendor> getVendorByCountries(@Param("countries") List<String> countries);

    public void insertVendorBusinessType(@Param("vendorId") Long vendorId, @Param("businessTypeId") Integer businessTypeId);

    void deleteVendorBusinessType(Long vendorId);

    List<String> getVendorRegions();

    public int deactivateSysVendorById(Long id);
}
