package edu.neu.ccs.cs5500.chucknorris.messenger.resources;

import edu.neu.ccs.cs5500.chucknorris.messenger.model.Profile;
import edu.neu.ccs.cs5500.chucknorris.messenger.service.ProfileService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by yoganandc on 5/31/16.
 */

@Path("/profiles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProfileResource {

    ProfileService profileService = new ProfileService();

    @GET
    public List<Profile> getProfiles() {
        return profileService.getAllProfiles();
    }

    @GET
    @Path("/{profileId}")
    public Profile getProfile(@PathParam("profileId") long profileId) {
        return profileService.getProfile(profileId);
    }

    @POST
    public Profile addProfile(Profile profile) {
        return profileService.addProfile(profile);
    }

    @PUT
    @Path("/{profileId}")
    public Profile updateProfile(@PathParam("profileId") long profileId, Profile profile) {
        profile.setId(profileId);
        return profileService.updateProfile(profile);
    }

    @DELETE
    @Path("/{profileId}")
    public void deleteProfile(@PathParam("profileId") long profileId) {
        profileService.removeProfile(profileId);
    }
}
