<!--- 
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 ---><cfcomponent extends="Driver" output="false" implements="IDriver">
	<cfset this.className="com.microsoft.jdbc.sqlserver.SQLServerDriver">
	<cfset this.dsn="jdbc:sqlserver://{host}:{port}">
		
	<cfset this.type.port=this.TYPE_FREE>
	<cfset this.value.host="localhost">
	<cfset this.value.port=1433>
	
	
	<cfset fields=array()>
	<cfset fields=array(
		field("Select Method","SelectMethod","direct,cursor",true,"A hint to the driver that determines whether the driver requests a database cursor for Select statements. Performance and behavior of the driver are affected by this property, which is defined as a hint because the driver may not always be able to satisfy the requested method.

    <ul><li>

      Direct�When the driver uses the Direct method, the database server sends the complete result set in a single response to the driver when responding to a query.

    <li>

      Cursor�When the driver uses the Cursor method, a server-side cursor is requested. The rows are retrieved from the server in blocks when returning forward-only result sets.</ul>","select")
	)>
	
	
	
	<cfset data=struct()>
	
	<cffunction name="onBeforeUpdate" returntype="void" output="no">
		<cfset form.custom_DatabaseName=form.database>
	</cffunction>
	
	<cffunction name="getFields" returntype="array" output="no"
		hint="returns array of fields">
		<cfreturn fields>
	</cffunction>
	
	<cffunction name="getClass" returntype="string" output="no"
		hint="return driver Java Class">
		<cfreturn this.className>
	</cffunction>
	
	<cffunction name="getDSN" returntype="string" output="no"
		hint="return DSN">
		<cfreturn this.dsn>
	</cffunction>
	
	<cffunction name="getName" returntype="string" output="no"
		hint="returns display name of the driver">
		<cfreturn "MSSQL - Microsoft SQL Server (Vendor Microsoft)">
	</cffunction>
	
	<cffunction name="getDescription" returntype="string" output="no"
		hint="returns description for the driver">
		<cfreturn "Microsoft SQL Server Driver">
	</cffunction>

	<cffunction name="equals" returntype="string" output="no"
		hint="return if String class match this">
		<cfargument name="className" required="true">
		<cfargument name="dsn" required="true">
		<cfreturn this.className EQ arguments.className and findNoCase("sqlserver",arguments.dsn)>
	</cffunction>
	
</cfcomponent>