package com.owl.dashBoard.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.owl.dashBoard.dao.DashBoardDao;
import com.owl.dashBoard.dto.IssueTask;
import com.owl.dashBoard.dto.ProjectProgress;
import com.owl.dashBoard.dto.TimeLine;

@Service
public class DashBoardService {
	@Autowired
	private SqlSession sqlSession;
	
	/**
	 * 멤버가 참여중인 프로젝트가 존재하는지 체크
	 * @author 윤다정
	 * @since 2020/01/31
	 * @param email
	 * @return int
	 */
	public int checkJoinProject(String email) {
		int result =0;
		DashBoardDao dao = getDashBoardDao();
		try {
			result = dao.checkJoinProject(email);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * dueData, priority 순으로 Issue Task 테이블 데이터 get
	 * @author 윤다정
	 * @since 2020/01/31
	 * @param projectidx
	 * @param assigned
	 * @return List<IssueTask>
	 */
	public List<IssueTask> getMyIssueTasks(String assigned) {
		DashBoardDao dao = getDashBoardDao();
		List<IssueTask> issueTasks = new ArrayList<IssueTask>();
		try {
			issueTasks = dao.getMyIssueTasks(assigned);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return issueTasks;
	}

	/**
	 * 본인에게 할당된 모든 이슈 진행률(프로젝트 별)
	 * @author 윤다정
	 * @since 2020/01/31
	 * @param email
	 * @return Map<Integer ,List<ProjectProgress>>
	 */
	public Map<Integer ,List<ProjectProgress>> getMyProjectChart(String email){
		DashBoardDao dao = getDashBoardDao();
		List<ProjectProgress> progress = new ArrayList<ProjectProgress>();
		Map<Integer, List<ProjectProgress>> results = new HashMap<Integer, List<ProjectProgress>>();
		try {
			progress = dao.getMyProjectChart(email);
			results=progress.stream().collect(Collectors.groupingBy(ProjectProgress::getProjectIdx));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}		
		
		return results;		
	}
	
	/**
	 * dueData, priority 순으로 Issue Task 테이블 데이터 get (프로젝트 안에서 본인 이슈)
	 * @author 윤다정
	 * @since 2020/02/01
	 * @param projectIdx
	 * @param assigned
	 * @return List<IssueTask>
	 */
	public List<IssueTask> getMyIssueTasksByProject(int projectIdx, String assigned) {
		DashBoardDao dao = getDashBoardDao();
		List<IssueTask> issueTasks = new ArrayList<IssueTask>();
		try {
			issueTasks = dao.getMyIssueTasksByProject(projectIdx, assigned);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return issueTasks;
	}
	
	/**
	 * dueData, priority 순으로 Issue Task 테이블 데이터 get (프로젝트 별)
	 * @author 윤다정
	 * @since 2020/02/01
	 * @param projectIdx
	 * @return List<IssueTask>
	 */
	public List<IssueTask> getProjectIssueTasks(int projectIdx) {
		DashBoardDao dao = getDashBoardDao();
		List<IssueTask> issueTasks = new ArrayList<IssueTask>();
		try {
			issueTasks = dao.getProjectIssueTasks(projectIdx);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return issueTasks;
	}
	
	/**
	 * 일주일 단위로 개인에게 할당된 이슈 TimeLine 데이터 get
	 * @author 윤다정
	 * @since 2020/01/31
	 * @param email
	 * @return Map<String, List<TimeLine>>
	 */
	public Map<String, List<TimeLine>> getMyTimeLines(String email) {
		DashBoardDao dao = getDashBoardDao();
		List<TimeLine> timeLines = new ArrayList<TimeLine>();
		Map<String, List<TimeLine>> results = new TreeMap<>();
		try {
			timeLines = dao.getMyTimeLines(email);
			results = timeLines.stream().collect(Collectors.groupingBy(TimeLine::getDueDate, TreeMap::new, Collectors.toList()));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	/**
	 * 본인에게 할당된 이슈 진행률(프로젝트 별 in Project)
	 * @author 이정은
	 * @since 2020/01/31
	 * @param assigned
	 * @param projectIdx
	 * @return List<ProjectProgress>
	 */
	public List<ProjectProgress> getProgressChart(String assigned, int projectIdx) {
		DashBoardDao dao = getDashBoardDao();
		List<ProjectProgress> progress = new ArrayList<ProjectProgress>();

		try {
			progress =dao.getProgressChart(assigned, projectIdx);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return progress;
	}
	
	
	/**
	 * 프로젝트 내의 할당된 이슈 진행률(프로젝트 별 in Project)
	 * @author 이정은
	 * @since 2020/01/31
	 * @param projectIdx
	 * @return List<ProjectProgress>
	 */
	public List<ProjectProgress> getProjectChart(int projectIdx) {
		DashBoardDao dao = getDashBoardDao();
		List<ProjectProgress> progress = new ArrayList<ProjectProgress>();
		try {
			progress =dao.getProjectChart(projectIdx);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return progress;
	}
	
	public Map<String ,List<ProjectProgress>> getLabelChart(int projectIdx){
		DashBoardDao dao = getDashBoardDao();
		List<ProjectProgress> progress = new ArrayList<ProjectProgress>();
		Map<String, List<ProjectProgress>> results = new HashMap<String, List<ProjectProgress>>();
		try {
			progress = dao.getLabelChart(projectIdx);
			results=progress.stream().collect(Collectors.groupingBy(ProjectProgress::getLabelName));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}		
		
		return results;		
	}
	
	/**
	 * DashBoardDao 구하기
	 * @author 윤다정
	 * @since 2020/01/31
	 * @return DashBoardDao
	 */
	private DashBoardDao getDashBoardDao() {
		return sqlSession.getMapper(DashBoardDao.class);
	}
}