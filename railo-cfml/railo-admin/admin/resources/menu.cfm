
<cffunction name="createMenu" returntype="array">
	<cfargument name="stMenu" type="struct" required="yes">
	<cfargument name="adminType" type="string" required="yes">
	<cfset var MenuStruct = 
	array(
		struct(
			action:"server",label:stMenu.server.label,
			children:array(
				struct(action:"cache",label:stMenu.server.cache),
				struct(action:"regional",label:stMenu.server.regional),
				struct(action:"charset",label:stMenu.server.charset),
				struct(action:"scope",label:stMenu.server.scope),
				struct(action:"application",label:stMenu.server.application),
				struct(action:"output",label:stMenu.server.output),
				struct(action:"error",label:stMenu.server.error)
			)
		),
		struct(
			action:"services",label:stMenu.services.label,
			children:array(
				struct(action:"gateway",label:stMenu.services.gateway,hidden: adminType NEQ "web"),
				struct(action:"cache",label:stMenu.services.cache),
				struct(action:"datasource",label:stMenu.services.datasource),
				struct(action:"orm",label:stMenu.services.orm),
				struct(action:"search",label:stMenu.services.search,hidden: adminType NEQ "web"),
				struct(action:"mail",label:stMenu.services.mail),
				struct(action:"tasks",label:stMenu.services.tasks),
				struct(action:"schedule",label:stMenu.services.schedule,hidden:adminType NEQ "web"),
				struct(action:"update",label:stMenu.services.update,hidden:adminType EQ "web",display:true),
				struct(action:"restart",label:stMenu.services.restart,hidden:adminType EQ "web",display:true),
				struct(action:"certificates",label:stMenu.services.certificates,hidden:adminType EQ "web",display:true)
				
			)
		),
		struct(
			action:"extension",label:stMenu.extension.label,
			children:array(
				struct(action:"applications",label:stMenu.extension.applications),
				struct(action:"providers",label:stMenu.extension.providers)
			)
		),
		struct(
			action:"remote",label:stMenu.remote.label,
			children:array(
				struct(action:"securityKey",label:stMenu.remote.securityKey,hidden:!request.hasRemoteClientUsage),
				struct(action:"clients",label:stMenu.remote.clients,hidden:!request.hasRemoteClientUsage)
			)
		),
		
		struct(
			action:"resources",label:stMenu.resources.label,
			children:array(
				struct(action:"mappings",label:stMenu.resources.mappings),
				struct(action:"component",label:stMenu.resources.component),
				struct(action:"customtags",label:stMenu.resources.customtags),
				struct(action:"cfx_tags",label:stMenu.resources.cfx_tags)
			)
		),
		struct(action:"development",label:stMenu.development.label,
			children:array(
				struct(action:"debugging",label:stMenu.development.debugging)
			)
		),
		struct(action:"security",label:stMenu.security.label,
			children:array(
				struct(action:"access",label:stMenu.security.access,hidden:adminType NEQ "server"),
				struct(action:"password",label:stMenu.security.password,display:true)
				,struct(action:"serial",label:stMenu.security.serial,hidden:adminType NEQ "server" or server.ColdFusion.ProductLevel NEQ "enterprise",display:true)
			)
		),
		struct(action:"documentation",label:stMenu.documentation.label,
			children:array(
				struct(action:"tagRef",label:stMenu.documentation.tagRef),
				struct(action:"funcRef",label:stMenu.documentation.funcRef)
			)
		)
	)>
    <cfreturn MenuStruct>
</cffunction>





