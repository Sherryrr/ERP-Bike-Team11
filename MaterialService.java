
/*
 * This class represents the MaterialService, which is a service layer for managing materials in an application.
 * It provides various methods for creating, retrieving, updating, and deleting materials, as well as retrieving
 * information about raw materials associated with materials. The class interacts with the MaterialRepository and
 * MaterialRawMaterialRepository to perform database operations related to materials and their associations with raw
 * materials. Additionally, it utilizes the RawMaterialService and LogService for specific functionality.
 */

package com.soen390.team11.service;

import com.soen390.team11.constant.LogTypes;
import com.soen390.team11.dto.MaterialRequestDto;
import com.soen390.team11.dto.RawMaterialRequestDto;
import com.soen390.team11.entity.Material;
import com.soen390.team11.entity.MaterialRawMaterials;
import com.soen390.team11.repository.MaterialRawMaterialRepository;
import com.soen390.team11.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Layer for Material
 */
@Service
public class MaterialService {

    MaterialRepository materialRepository;
    MaterialRawMaterialRepository materialrawmaterialRepository;
    RawMaterialService rawmaterialService;
    LogService logService;

    public MaterialService(MaterialRepository materialRepository, MaterialRawMaterialRepository materialrawmaterialRepository, RawMaterialService rawmaterialService
    , LogService logService) {
        this.materialRepository = materialRepository;
        this.materialrawmaterialRepository = materialrawmaterialRepository;
        this.rawmaterialService = rawmaterialService;
        this.logService = logService;
    }

    /**
     * Creates a new Material
     *
     * @param materialRequestDto The Material Request
     * @return The created Material
     */
    public Material createMaterial(MaterialRequestDto materialRequestDto){
        logService.writeLog(LogTypes.MATERIAL,"Creating Material");
        return materialRepository.save(materialRequestDto.getMaterial());
    }

    /**
     * Gets all Materials
     *
     * @return List of Materials
     */
    public List<Material> getAllMaterial() {
        logService.writeLog(LogTypes.MATERIAL,"Returning all materials");
        return (List<Material>) materialRepository.findAll();
    }

    /**
     * Gets a specific Material
     *
     * @param id Material's ID
     * @return The Material
     */
    public Material getMaterialById(String id) {
        try {
            logService.writeLog(LogTypes.MATERIAL,"Getting material using ID");
            Material material= materialRepository.findById(id).get();
            return material;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Deletes a Material
     *
     * @param id The Material's ID
     * @return success message
     * @throws Exception Thrown exception if there is an invalid ID
     */
    
    // Deletes a material with the specified ID from the database.
    public String deleteMaterial(String id) throws Exception {
        // Checks if the material with the specified ID exists in the database.
        if(getMaterialById(id)==null) {
            throw new Exception("invalid id");
        }
        materialRepository.deleteById(id);
        logService.writeLog(LogTypes.MATERIAL,"Material Deleted successfully");
        return "success";
    }

    /**
     * Update a Material
     *
     * @param id The Material's ID
     * @param materialRequestDto The Material's Details
     * @return The updated Material
     * @throws Exception Thrown exception if there is an invalid ID
     */
    public Material updateMaterial(String id, MaterialRequestDto materialRequestDto) throws Exception {
        Material material = materialRequestDto.getMaterial();
        if(getMaterialById(id) ==null) {
            throw new Exception("invalid id");
        }
        logService.writeLog(LogTypes.MATERIAL,"Updating Material ID");
        material.setMaterialid(id);
        return materialRepository.save(material);
    }

    /**
     * Gets All Materials
     *
     * @return List of all Materials
     */
    public List<Material> getAllMaterialsIninventory(){
        List<Material> Materiallist= new ArrayList<>();
        materialRepository.findAll()
                .forEach(Materiallist::add);
        logService.writeLog(LogTypes.MATERIAL,"Returning full material inventory");
        return Materiallist;
    }

    /**
     * Gets list of Material's Raw Materials
     *
     * @param materialid The Material's ID
     * @return List of Raw Materials
     */
    
    // Retrieves a list of raw materials associated with a specific material ID.
    public List<RawMaterialRequestDto> getAllMaterialRawMaterial(String materialid){
        List<RawMaterialRequestDto> rawMaterial= new ArrayList<>();
        List<MaterialRawMaterials> MaterialRawMaterialsList = materialrawmaterialRepository.findByMaterialRawMaterialsIdRawmaterialid(materialid);
        // Iterates over the list of MaterialRawMaterials to retrieve the corresponding RawMaterialRequestDto objects.
        for(MaterialRawMaterials materialRawMaterials : MaterialRawMaterialsList){
            rawMaterial.add(rawmaterialService.getRawMaterialById(materialRawMaterials.getMaterialRawMaterialsId().getRawmaterialid()));
        }
        logService.writeLog(LogTypes.MATERIAL,"Returning all raw material in inventory");
        return rawMaterial;
    }
}
