package com.codefellowship.codefellowship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;

@Controller
public class ApplicationUserController {

    @Autowired
    ApplicationUserRepository applicationUserRepository;

//    @Autowired
//    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String getSignUpPage(){
        return "signup";
    }


    @PostMapping("/signup")
    public RedirectView signup(String username,
                               String password,
                               String firstName,
                               String lastName,
                               String dateOfBirth,
                               String bio){

        ApplicationUser newUser = new ApplicationUser(username,passwordEncoder.encode(password),firstName,lastName,dateOfBirth,bio);
        newUser = applicationUserRepository.save(newUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser,null,new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new RedirectView("/login");
    }


    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }


    @GetMapping("/users/{id}")
    public String getUserPage(Principal p,Model m, @PathVariable long id){
        try {
            String username = p.getName();
            ApplicationUser currentUser = applicationUserRepository.findByUsername(username);
            ApplicationUser user = applicationUserRepository.findById(id).get();
            System.out.println(currentUser.getUsers());
            // if followed see the button as un follow..
            boolean isFollowed = currentUser.isFollowedUser(user);
            boolean isSameUser = false;
            if(username.equals(user.getUsername()))
                isSameUser = true;
            m.addAttribute("user", user);
            m.addAttribute("isSameUser", isSameUser);
            m.addAttribute("username", username);
            m.addAttribute("isFollowed", isFollowed);
            return "users";
        }
        catch(Exception e){
            return "users";
        }
    }

    @PostMapping("/followUser/{username}")
    public RedirectView followUser(Principal p,@PathVariable String username){
        ApplicationUser currentUser = applicationUserRepository.findByUsername(p.getName());
        ApplicationUser userWantedToFollow = applicationUserRepository.findByUsername(username);
        currentUser.addFollowToUser(userWantedToFollow);
        applicationUserRepository.save(currentUser);
        return new RedirectView("/users/"+userWantedToFollow.getId());
    }
    @PostMapping("/unfollowUser/{username}")
    public RedirectView unFollowUser(Principal p,@PathVariable String username){
        ApplicationUser currentUser = applicationUserRepository.findByUsername(p.getName());
        ApplicationUser userWantedToUnFollow = applicationUserRepository.findByUsername(username);
        currentUser.unFollowUser(userWantedToUnFollow);
        applicationUserRepository.save(currentUser);
        return new RedirectView("/users/"+userWantedToUnFollow.getId());
    }



    @GetMapping("/myprofile")
    public RedirectView displayMyProfile(Principal p){
        String name = p.getName();
        ApplicationUser user = applicationUserRepository.findByUsername(name);
        long id = user.getId();
        return new RedirectView("/users/"+id);
    }



    @GetMapping("/")
    public String getHomePage (Principal p,Model m){
        if(p != null)
             m.addAttribute("username",p.getName());
        return ("index");
    }
}
