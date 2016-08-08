package com.momathink.crm.proxyproject.controller;

import java.util.List;

import org.apache.log4j.Logger;

import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.tools.ToolOperatorSession;
import com.momathink.crm.proxyproject.model.CooperationProject;
import com.momathink.crm.proxyproject.model.ProxyProject;
import com.momathink.crm.proxyproject.service.ProxyProjectService;
import com.momathink.sys.system.model.SysUser;

@Controller( controllerKey="/proxy/proxyproject" )
public class ProxyProjectController extends BaseController {

	private static final Logger log = Logger.getLogger( ProxyProjectController.class );
	private static final ProxyProjectService projectService = new ProxyProjectService();
	
	/**
	 * 我的合作项目
	 */
	public void projectList() {
		Integer sysUserId = getSysuserId();
		SysUser account = SysUser.dao.findById( sysUserId );
		boolean firstAgents = ToolOperatorSession.judgeRole( "firstagents" , account.getStr( "roleids" ) );
		boolean secondAgents = ToolOperatorSession.judgeRole( "secondagents" , account.getStr( "roleids" ) );
		setAttr( "firstAgents" , firstAgents );
		setAttr( "secondAgents" , secondAgents );
		
		if( firstAgents || secondAgents ) {
			projectService.cooperatedProjectList( splitPage , sysUserId );
		} else {
			projectService.cooperationProjectList( splitPage , sysUserId );
		}
		renderJsp( "projectlist.jsp" );
	}
	
	
	/**
	 * 甲方操作代理的合作项目
	 */
	public void proxyproject() {
		try {
			String proxyId = getPara();
			List< ProxyProject > proxyProject = ProxyProject.dao.currentProxyProjectList( proxyId );
			setAttr( "proxyProject" , proxyProject );
			
			List< CooperationProject > allRemainProject = CooperationProject.dao.AllProjectNotInProxy( proxyId );
			setAttr( "allRemain" , allRemainProject );
			
			renderJsp( "proxyproject.jsp" );
		} catch (Exception e) {
			log.error( "cooperationProject" , e );
			renderError( 500 );
		}
	}
	
	
	
	
	
	
}
