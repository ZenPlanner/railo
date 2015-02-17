package railo.runtime.type.scope;

import railo.runtime.type.Scope;

import java.util.HashSet;
import java.util.Set;


/**
 * creates Local and Argument scopes and recyle it
 */
public final class ScopeFactory {
    
    int argumentCounter=0;
    ArgumentPro[] arguments=new ArgumentPro[]{
            new ArgumentImpl(),
            new ArgumentImpl(),
            new ArgumentImpl(),
            new ArgumentImpl(),
            new ArgumentImpl(),
            new ArgumentImpl(),
            new ArgumentImpl(),
            new ArgumentImpl(),
            new ArgumentImpl(),
            new ArgumentImpl(),
            new ArgumentImpl(),
            new ArgumentImpl(),
            new ArgumentImpl(),
            new ArgumentImpl(),
            new ArgumentImpl(),
            new ArgumentImpl(),
            new ArgumentImpl(),
            new ArgumentImpl(),
            new ArgumentImpl()
    };
    
    int localCounter=0;
    LocalImpl[] locals=new LocalImpl[]{
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl(),
            new LocalImpl()
    };
    Set<Scope> ownedScopes = new HashSet<Scope>();

    public ScopeFactory() {
        for(LocalImpl scope : locals) {
            ownedScopes.add(scope);
        }
    }
    
    /**
     * @return returns a Argument scope
     */
    public Argument getArgumentInstance() {
        if(argumentCounter<arguments.length) {
        	return arguments[argumentCounter++];
        }
        return new ArgumentImpl();
    }

    /**
     * @return retruns a Local Instance
     */
    public LocalImpl getLocalInstance() {
        if(localCounter<locals.length) {
            return locals[localCounter++];
        }
        return new LocalImpl();
    }
    
    /** 
     * @param argument  recycle a Argument scope for reuse
     */
    public void recycle(ArgumentPro argument) {
        if(argumentCounter<=0 || argument.isBind()) return;
        argument.release();
        arguments[--argumentCounter]=argument;
    }

    int negLogCount = 0;

    /**
     * @param local recycle a Local scope for reuse
     */
    public void recycle(LocalImpl local) {
        if(negLogCount < 10 && localCounter <= 0) {
            System.out.println("Released scope with counter at " + localCounter);
            negLogCount++; // Don't kill the server with a million of these
        }
        if(!ownedScopes.contains(local)) {
            System.out.println("Releasing a scope not owned by this ScopeFactory!");
        }
        if (localCounter <= 0 || local.isBind())
            return;
        local.release();
        locals[--localCounter] = local;
    }
    
    /**
     * cast a int scope definition to a string definition
     * @param scope
     * @return
     */
    public static String toStringScope(int scope, String defaultValue) {
        switch(scope) {
        case Scope.SCOPE_APPLICATION:   return "application";
        case Scope.SCOPE_ARGUMENTS:     return "arguments";
        case Scope.SCOPE_CALLER:        return "caller";
        case Scope.SCOPE_CGI:           return "cgi";
        case Scope.SCOPE_CLIENT:        return "client";
        case Scope.SCOPE_COOKIE:        return "cookie";
        case Scope.SCOPE_FORM:          return "form";
        case ScopeSupport.SCOPE_VAR:         
        case Scope.SCOPE_LOCAL:         return "local";
        case Scope.SCOPE_REQUEST:       return "request";
        case Scope.SCOPE_SERVER:        return "server";
        case Scope.SCOPE_SESSION:       return "session";
        case Scope.SCOPE_UNDEFINED:     return "undefined";
        case Scope.SCOPE_URL:           return "url";
        case Scope.SCOPE_VARIABLES:     return "variables";
        case ScopeSupport.SCOPE_CLUSTER:     return "cluster";
        }
        
        
        
        return defaultValue;
    }
    
    /* *
     * return a string list of all scope names
     * @param orderAsInvoked when true the order of the list is the same as they are invoked by the Undefined Scope, when the value is false the list is returned in a alphabetic order 
     * @return
     * /
    public static String[] getAllScopes(boolean orderAsInvoked) {
    	if(!orderAsInvoked){
    		return new String[]{
        			"application","arguments","caller","cgi","client","cluster","cookie","form","local","request","server","session","url","variables"
            };
    	}
    	return new String[]{
    			"local","arguments","variables","cgi","url","form","cookie","client","application","caller","cluster","request","server","session"
        };
    }*/

}