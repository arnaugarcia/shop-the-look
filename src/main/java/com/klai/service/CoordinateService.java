package com.klai.service;

import com.klai.domain.Coordinate;
import com.klai.repository.CoordinateRepository;
import com.klai.service.dto.CoordinateDTO;
import com.klai.service.mapper.CoordinateMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Coordinate}.
 */
@Service
@Transactional
public class CoordinateService {

    private final Logger log = LoggerFactory.getLogger(CoordinateService.class);

    private final CoordinateRepository coordinateRepository;

    private final CoordinateMapper coordinateMapper;

    public CoordinateService(CoordinateRepository coordinateRepository, CoordinateMapper coordinateMapper) {
        this.coordinateRepository = coordinateRepository;
        this.coordinateMapper = coordinateMapper;
    }

    /**
     * Save a coordinate.
     *
     * @param coordinateDTO the entity to save.
     * @return the persisted entity.
     */
    public CoordinateDTO save(CoordinateDTO coordinateDTO) {
        log.debug("Request to save Coordinate : {}", coordinateDTO);
        Coordinate coordinate = coordinateMapper.toEntity(coordinateDTO);
        coordinate = coordinateRepository.save(coordinate);
        return coordinateMapper.toDto(coordinate);
    }

    /**
     * Partially update a coordinate.
     *
     * @param coordinateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CoordinateDTO> partialUpdate(CoordinateDTO coordinateDTO) {
        log.debug("Request to partially update Coordinate : {}", coordinateDTO);

        return coordinateRepository
            .findById(coordinateDTO.getId())
            .map(
                existingCoordinate -> {
                    coordinateMapper.partialUpdate(existingCoordinate, coordinateDTO);

                    return existingCoordinate;
                }
            )
            .map(coordinateRepository::save)
            .map(coordinateMapper::toDto);
    }

    /**
     * Get all the coordinates.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CoordinateDTO> findAll() {
        log.debug("Request to get all Coordinates");
        return coordinateRepository.findAll().stream().map(coordinateMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one coordinate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CoordinateDTO> findOne(Long id) {
        log.debug("Request to get Coordinate : {}", id);
        return coordinateRepository.findById(id).map(coordinateMapper::toDto);
    }

    /**
     * Delete the coordinate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Coordinate : {}", id);
        coordinateRepository.deleteById(id);
    }
}
