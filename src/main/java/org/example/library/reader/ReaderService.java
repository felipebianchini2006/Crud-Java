package org.example.library.reader;

import java.util.List;
import org.example.library.common.BusinessException;
import org.example.library.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReaderService {

    private final ReaderRepository repository;

    public ReaderService(ReaderRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ReaderResponse create(ReaderRequest request) {
        repository.findByEmail(request.email()).ifPresent(reader -> {
            throw new BusinessException("E-mail já cadastrado para outro leitor.");
        });
        Reader reader = new Reader(request.name(), request.email(), request.phone());
        return ReaderResponse.from(repository.save(reader));
    }

    @Transactional(readOnly = true)
    public List<ReaderResponse> findAll() {
        return repository.findAll().stream()
                .map(ReaderResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReaderResponse findById(Long id) {
        Reader reader = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leitor", id));
        return ReaderResponse.from(reader);
    }

    @Transactional
    public ReaderResponse update(Long id, ReaderRequest request) {
        Reader reader = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leitor", id));
        repository.findByEmail(request.email())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessException("E-mail já cadastrado para outro leitor.");
                });
        reader.setName(request.name());
        reader.setEmail(request.email());
        reader.setPhone(request.phone());
        return ReaderResponse.from(reader);
    }

    @Transactional
    public void delete(Long id) {
        Reader reader = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leitor", id));
        repository.delete(reader);
    }
}