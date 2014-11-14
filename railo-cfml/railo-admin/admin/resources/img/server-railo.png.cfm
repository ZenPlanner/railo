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
				
		<cfset etag 	= '''9EED47EF951895ACA54AEEE6B939C841'''>
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