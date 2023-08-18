package org.foundation.atomjoblistingservice.service;

import lombok.RequiredArgsConstructor;
import org.foundation.atomjoblistingservice.dto.JobListingFlatResponseDTO;
import org.foundation.atomjoblistingservice.dto.JobListingRequestDTO;
import org.foundation.atomjoblistingservice.entity.JobListing;
import org.foundation.atomjoblistingservice.repository.JobListingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JobListingService {
    private final JobListingRepository jobListingRepository;
    private final ModelMapper modelMapper;

    public JobListingFlatResponseDTO createJobListing(final JobListingRequestDTO jobListingRequestDTO) {
        final JobListing jobListing = modelMapper.map(jobListingRequestDTO, JobListing.class);
        return modelMapper.map(jobListingRepository.save(jobListing), JobListingFlatResponseDTO.class);
    }

    public Page<JobListingFlatResponseDTO> findAllJobListings(final int page, final int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        return jobListingRepository.findAll(pageRequest).map(jobListing -> modelMapper.map(jobListing, JobListingFlatResponseDTO.class));
    }
}
