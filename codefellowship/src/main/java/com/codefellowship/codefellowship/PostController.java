package com.codefellowship.codefellowship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PostController {

    @Autowired
    PostRepository postRepository;
    @Autowired
    ApplicationUserRepository applicationUserRepository;
    @GetMapping("/profile")
    public String getAllPosts(Model m, Principal p){
        String username = p.getName();
        ApplicationUser user = applicationUserRepository.findByUsername(username);
        long id = user.getId();
        List<Post> posts = postRepository.findAllByUserId(id);

        m.addAttribute("posts",posts);
        m.addAttribute("username",p.getName());
        return "profile";
    }

    @GetMapping("/feed")
    public String showUserFeed(Model m, Principal p){
        String username = p.getName();
        ApplicationUser user = applicationUserRepository.findByUsername(username);
        List<Long> ids = user.getUsers().stream().map(u -> u.getId()).collect(Collectors.toList());
        List < List<Post> > postsOfUsers = ids.stream().map(i -> postRepository.findAllByUserId(i)).collect(Collectors.toList());
        System.out.println(postsOfUsers);
        m.addAttribute("postsOfUsers",postsOfUsers);
        m.addAttribute("username",p.getName());
        return "feed";
    }

    @PostMapping("/addPost")
    public RedirectView createPost(Principal p, String body){
        String username = p.getName();
        ApplicationUser user = applicationUserRepository.findByUsername(username);
        Post newPost = new Post(body,user);
        postRepository.save(newPost);

        return new RedirectView("/profile");
    }

    @GetMapping("/addPost")
    public String getAddPostPage(){
        return "addPost";
    }


}
