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
 ---><cfif structKeyExists(form,'mainAction')>
	<cfset error.message="">
    <cfset error.detail="">
    <!--- actions --->
    <cftry>
        <cfif form.mainAction EQ stText.Buttons.reset>
        	<cfadmin 
                action="resetId"
                type="#request.adminType#"
                password="#session["password"&request.adminType]#">
				<!--- remoteClients="#request.getRemoteClients()#" --->
        </cfif>
    
        <cfcatch>
            <cfset error.message=cfcatch.message>
            <cfset error.detail=cfcatch.Detail>
        </cfcatch>
    </cftry>
    
	<!--- redirect --->
    <cfif cgi.request_method EQ "POST" and error.message EQ "">
        <cflocation url="#request.self#?action=#url.action#" addtoken="no">
    </cfif>
    
    <!--- error ---->
    <cfset printError(error)>
</cfif>

<cfoutput>
	<div class="pageintro">
		#sttext.remote.securityKeyTitleDesc#
	</div>
	<div class="center">
		<input type="text" id="remotekey" value="#getRailoId()[request.adminType].securityKey#" size="50" readonly="readonly" />
		<script type="text/javascript">
			$(function(){
				$('##remotekey').bind('focus keydown', function(){ $(this).select() });
			});
		</script>
	</div>
	
	<h2>Reset the security key</h2>
	<cfform onerror="customError" action="#request.self#?action=#url.action#" method="post">
		<input type="submit" class="button submit" name="mainAction" value="#stText.Buttons.reset#">
	</cfform>
</cfoutput>


