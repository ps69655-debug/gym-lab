package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.MembershipType;

import java.util.List;

public interface MembershipTypeService {
    List<MembershipType> findAll();
    MembershipType findById(Long id);
    MembershipType save(MembershipType type);
    void deleteById(Long id);
}