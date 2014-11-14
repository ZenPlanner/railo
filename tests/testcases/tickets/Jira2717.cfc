/**
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
 **/
<cfscript>
component extends="org.railo.cfml.test.RailoTestCase"	{
	
	//public function beforeTests(){}
	
	//public function afterTests(){}
	
	//public function setUp(){}
	
	public void function testRegularFunctionCall(){
		var testArray = [ ];
		arrayAppend( testArray, nullvalue() );
		assertEquals(1,arrayLen(testArray));
	}
	public void function testEvaluateRegularFunctionCall(){
		var testArray = [ ];
		evaluate('arrayAppend( testArray, nullvalue() )');
		assertEquals(1,arrayLen(testArray));
	}
	
	public void function testMemberFunctionCall(){
		var testArray = [ ];
		testArray.append( nullvalue());
		assertEquals(1,arrayLen(testArray));
	}
	
	public void function testEvaluateMemberFunctionCall(){
		var testArray = [ ];
		evaluate('testArray.append( nullvalue() )');
		assertEquals(1,arrayLen(testArray));
	}
} 
</cfscript>