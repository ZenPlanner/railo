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
 ---><cfcomponent name="Field" entityname="Field" table="field2275" persistent="true" output="false" accessors="true" hint="Field Placeholder cfc">
	<cfproperty name="ID" column="id" fieldtype="id" type="string" ormtype="string" length="18" update="false" insert="false"  />
	<cfproperty name="Name" column="name" sqltype="nvarchar(50)"  />
	<cfproperty name="CustomFieldType" column="type" sqltype="nvarchar(50)" /> 
	
	<cffunction name="init" output="false" access="public" returntype="Field">
		<cfset setID(Left(replace(CreateUUID(),"-","","ALL"),18)) />
		<cfreturn this />
	</cffunction>
</cfcomponent>