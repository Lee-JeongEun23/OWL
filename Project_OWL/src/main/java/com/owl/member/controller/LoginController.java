package com.owl.member.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.google.connect.GoogleOAuth2Template;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.owl.member.dto.Member;
import com.owl.member.service.GoogleService;
import com.owl.member.service.KaKaoService;
import com.owl.member.service.MemberService;
import com.owl.member.service.NaverService;
import com.owl.project.dto.ProjectList;
import com.owl.project.service.ProjectService;

@Controller
public class LoginController {

	@Autowired
	private MemberService service;
	
	@Autowired
	private ProjectService ProjectSerivce;

	


	@Autowired
	private KaKaoService kaKaoService;

	@Autowired
	private NaverService naverService;
	
	@Autowired
	private GoogleService googleService;
	
    @Autowired
    private GoogleOAuth2Template googleOAuth2Template;
    
    @Autowired
    private OAuth2Parameters googleOAuth2Parameters;

	@RequestMapping(value = "Login.do", method = RequestMethod.GET)
	public String showView(HttpSession session,Model model) {
		String naverUrl = naverService.getAuthorizationUrl(session);
		String kakaoUrl = kaKaoService.getAuthorizationUrl();
		String googleUrl = googleOAuth2Template.buildAuthenticateUrl(GrantType.AUTHORIZATION_CODE, googleOAuth2Parameters);
		
		model.addAttribute("naverUrl", naverUrl);
		model.addAttribute("kakaoUrl", kakaoUrl);
        model.addAttribute("googleUrl", googleUrl);
        
		return "member/login";
	}
	
	@RequestMapping(value = "Main.do")
	public String showMainView(HttpServletRequest request, Principal principal, Model model) {
		System.out.println("principal : " + principal.getName());
		Member member = service.getMember(principal.getName());
		request.getSession().setAttribute("member", member);
		request.getSession().setAttribute("setting", service.getSetting(principal.getName()));
		
		List<ProjectList> projectList = null;
		projectList = ProjectSerivce.getProjectLists(member.getEmail());
		model.addAttribute("projectList", projectList);


		return "member/main";
	}
	
	@RequestMapping(value = "Login.do", method = RequestMethod.POST)
	public String login(HttpServletRequest request) {
		// for test
		request.getSession().setAttribute("member", service.getMember("qqq@gmail.com"));
		System.out.println("login");
		return "member/main";
	}
	
	@RequestMapping(value = "ReturnLogin.do")
	public String returnLogin() {
		return "member/login";
	}

	@RequestMapping(value = "KakaoLogin.do", produces = "application/json")
	public String kakaoLogin(@RequestParam("code") String code, HttpSession session) throws Exception {
		Member member = kaKaoService.getMemberInfo(code);

		if (member != null && !member.getEmail().isEmpty()) 
			snsJoinCheck(member);
		
		return "member/main";
	}


	@RequestMapping("NaverLogin.do")
	public String naverLogin(@RequestParam String code, @RequestParam String state, HttpSession session, Model model)
			throws IOException {
		Member member = naverService.getMemberInfo(session, code, state);
		
		if (member != null && !member.getEmail().isEmpty()) 
			snsJoinCheck(member);

		return "member/main";
	}
	
	private void snsJoinCheck(Member member) {
		if (service.getMember(member.getEmail()) == null)
			service.insertMember(member);
	}

	//구글 로그인 Controller
	@RequestMapping(value = "GoogleLogin.do")
    public String doSessionAssignActionPage(String code, HttpServletRequest request, Model model) throws Exception {
		Member member= googleService.getMemberInfo(code);
	
		if (member != null && !member.getEmail().isEmpty()) 
			snsJoinCheck(member);

        return "member/main"; 
    }

	@RequestMapping("Lock.do")
	public String showLockView() {
		return "member/lock";
	}
}
