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
 ---><cfsavecontent variable='content'>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</cfsavecontent>

	<cfsetting showdebugoutput='#false#'>
	<cfif getBaseTemplatePath() == getCurrentTemplatePath()>	

		<cfapplication name='__RAILO_STATIC_CONTENT' sessionmanagement='#false#' clientmanagement='#false#' applicationtimeout='#createtimespan( 1, 0, 0, 0 )#'>
				
		<cfset etag 	= '''864CAAA3F77CAFD195925CF779EAB450'''>
		<cfset mimetype = 'image/png'>		

		<cfheader name='Expires' value='#getHttpTimeString( now() + 100 )#'>
		<cfheader name='Cache-Control' value='max-age=#86400 * 100#'>		
		<cfheader name='ETag' value='#etag#'>

		<cfif len( CGI.HTTP_IF_NONE_MATCH ) && ( CGI.HTTP_IF_NONE_MATCH == '#etag#' )>

			<!--- etag matches, return 304 !--->
			<cfheader statuscode='304' statustext='Not Modified'>
			<cfcontent reset='#true#' type='#mimetype#'><cfabort>
		</cfif>

		<!--- file was not cached; send the content !--->
		<cfcontent reset='#true#' type='#mimetype#' variable='#toBinary( content )#'><cfabort>
	<cfelse>

		<cfcontent reset='#true#'><cfoutput>content:image/png;base64,#content#</cfoutput><cfabort>
	</cfif>