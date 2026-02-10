package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.GymPass;

import java.util.List;

public interface GymPassService {
    List<GymPass> findAll();
    List<GymPass> findByMemberId(Long memberId);

    GymPass findById(Long id);

    /**
     * Saves gym pass without changing member mapping (used e.g. for check-in updates).
     */
    GymPass save(GymPass gymPass);

    /**
     * Saves a gym pass and (if needed) creates/links a Member based on ownerName.
     * NOTE: This is legacy behavior in the current app; later (Pomysł A) we will
     * replace it with explicit user/member selection.
     */
    GymPass saveWithMember(GymPass gymPass);

    void deleteById(Long id);
}
