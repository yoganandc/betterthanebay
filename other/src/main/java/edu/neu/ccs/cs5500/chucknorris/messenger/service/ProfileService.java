package edu.neu.ccs.cs5500.chucknorris.messenger.service;

import edu.neu.ccs.cs5500.chucknorris.messenger.database.MessengerDB;
import edu.neu.ccs.cs5500.chucknorris.messenger.model.Profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yoganandc on 5/31/16.
 */
public class ProfileService {

    private Map<Long, Profile> profiles = MessengerDB.getProfiles();

    public List<Profile> getAllProfiles() {
        return new ArrayList<Profile>(profiles.values());
    }

    public Profile getProfile(long profileId) {
        return profiles.get(profileId);
    }

    public Profile addProfile(Profile profile) {
        profile.setId(MessengerDB.getNextProfileId());
        profile.setUpdated(new Date());
        profiles.put(profile.getId(), profile);
        return profile;
    }

    public Profile updateProfile(Profile profile) {
        if (profile.getId() < 0) {
            return null;
        }
        profile.setUpdated(new Date());
        profiles.put(profile.getId(), profile);
        return profile;
    }

    public Profile removeProfile(long profileId) {
        return profiles.remove(profileId);
    }
}
