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
component extends="org.railo.cfml.test.RailoTestCase"	{

	public function setUp(){
		file action="touch" file="#getCurrentTemplatePath()#";
	}

	public void function testPrecisionEvaluateString(){
		
		var p1 = 23.74;
		var p2 = 37.05;

		var pe = precisionEvaluate('p1 + p2');
		assertEquals( pe, 60.79 );
	}
	
	public void function testPrecisionEvaluate(){
		
		var p1 = 23.74;
		var p2 = 37.05;

		var pe = precisionEvaluate(p1 + p2);
		assertEquals( pe, 60.79 );
	}
	
	
	
	public void function testPrecisionEvaluateIncrement(){
		
		var p1 = 22.74;
		var p2 = 37.05;

		var pe = precisionEvaluate(++p1 + p2);
		assertEquals( pe, 60.79 );
	}
	
	
	public void function testPrecisionEvaluateDecrement(){
		
		var p1 = 24.74;
		var p2 = 37.05;

		var pe = precisionEvaluate(--p1 + p2);
		assertEquals( pe, 60.79 );
	}
	
	public void function testPrecisionEvaluateAssign(){
		
		var p1 = 23.74;
		var p2 = 37.05;

		var pe = precisionEvaluate((p3=p1 + p2));
		assertEquals( pe, 60.79 );
	}
	
}