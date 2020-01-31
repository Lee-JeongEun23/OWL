package com.owl.dashBoard.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.owl.dashBoard.dto.IssueTask;
import com.owl.dashBoard.service.DashBoardService;

@RestController
public class DashBoardRestController {
	
	@Autowired
	private DashBoardService service;
	
	/**
	 * MyDashBoard - 해당 멤버가 참여중인 프로젝트가 존재하는지 체크 요청
	 * @author 윤다정
	 * @since 2020/01/31
	 * @param principal
	 * @return int
	 */
	@RequestMapping("CheckJoinProject.do")
	public int checkJoinProject(Principal principal) {
		return service.checkJoinProject(principal.getName());
	}
	
	/**
	 * MyDashBoard - Issue Task 테이블 데이터 요청
	 * @author 윤다정
	 * @since 2020/01/31
	 * @param principal
	 * @return List<IssueTask>
	 */
	@RequestMapping("GetMyIssueTask.do")
	public List<IssueTask> getMyIssueTasks(Principal principal) {
		return service.getMyIssueTasks(principal.getName());
	}
}
