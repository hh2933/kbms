/**
 * Copyright (c)2005-2007 Matt Kruse (javascripttoolbox.com)
 * 
 * Dual licensed under the MIT and GPL licenses. 
 * This basically means you can use this code however you want for
 * free, but don't claim to have written it yourself!
 * Donations always accepted: http://www.JavascriptToolbox.com/donate/
 * 
 * Please do not link to the .js files on javascripttoolbox.com from
 * your site. Copy the files locally to your server instead.
 * 
 */
//eval(function(p,a,c,k,e,d){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--){d[e(c)]=k[c]||e(c)}k=[function(e){return d[e]}];e=function(){return'\\w+'};c=1};while(c--){if(k[c]){p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c])}}return p}('4 12=(b(){4 E=Z;4 h=-1;4 H=0;4 p=1;4 s=" ";4 l="\\n";4 j=q;4 r=q;b A(a,P){4 x=O Y();u(4 i=P;i<a.g;i++){x[x.g]=a[i]}7 x};b m(W){4 5="";u(4 i=0;i<W;i++){5+=s}7 5};b 8(o){4 k=1;4 e=p;4 5="";3(9.g>1&&c(9[1])=="F"){k=9[1];e=9[2];3(o==j){7"[17 j]"}}t{H=0;j=o;3(9.g>1){4 y=9;4 C=1;3(c(9[1])=="j"){y=9[1];C=0}u(4 i=C;i<y.g;i++){3(r==q){r=O 15()}r[y[i]]=1}}}3(H++>E){7"[13 16 14]"}3(h!=-1&&k>(h+1)){7"..."}3(c(o)=="K"){7"[K]"}3(o==q){7"[q]"}3(o==v){7"[v]"}3(o==v.f){7"[f]"}3(c(o)=="b"){7"[b]"}3(c(o)=="11"){7(o)?"B":"L"}3(c(o)=="8"){7"\'"+o+"\'"}3(c(o)=="F"){7 o}3(c(o)=="j"){3(c(o.g)=="F"){3(h!=-1&&k>h){7"[ ... ]"}5="[";u(4 i=0;i<o.g;i++){3(i>0){5+=","+l+m(e)}t{5+=l+m(e)}5+=8(o[i],k+1,e-0+p)}3(i>0){5+=l+m(e-p)}5+="]";7 5}t{3(h!=-1&&k>h){7"{ ... }"}5="{";4 I=0;u(i 10 o){3(o==j&&r!=q&&r[i]!=1){}t{3(c(o[i])!="1g"){4 z=B;3(o.1i||o.J||(o.N&&o.M)){z=L;3(i=="J"||i=="M"||i=="N"||i=="1h"||i=="18"){z=B}}3(z){3(I++>0){5+=","+l+m(e)}t{5+=l+m(e)}5+="\'"+i+"\' => "+8(o[i],k+1,e-0+i.g+6+p)}}}}3(I>0){5+=l+m(e-p)}5+="}";7 5}}};8.1a=b(o){4 w=v.X("19:1b");w.f.X();w.f.D("<Q><V><U>");w.f.D(8(o,A(9,1)));w.f.D("</U></V></Q>");w.f.1c()};8.R=b(o){R(8(o,A(9,1)))};8.T=b(o){4 G=1;4 d=f;3(9.g>1&&9[1]==v.f){d=9[1];G=2}4 S=s;s="&1d;";d.T(8(o,A(9,G)));s=S};8.1f=b(i){E=i};8.1e=b(i){h=i};8.$1j=1.0;7 8})();',62,82,'|||if|var|ret||return|string|arguments||function|typeof||indentLevel|document|length|maxDepth||object|level|newline|pad|||indent|null|properties|indentText|else|for|window||myargs|list|processAttribute|args|true|listIndex|writeln|maxIterations|number|argumentsIndex|iterations|count|tagName|undefined|false|nodeName|nodeType|new|index|HTML|alert|temp|write|PRE|BODY|len|open|Array|1000|in|boolean|Dumper|Max|Reached|Object|Iterations|original|className|about|popup|blank|close|nbsp|setMaxDepth|setMaxIterations|unknown|id|ownerDocument|VERSION'.split('|'),0,{}))
var Dumper = (function() {
    var maxIterations = 1000;
    var maxDepth = -1;
    var iterations = 0;
    var indent = 1;
    var indentText = " "; 
    var newline = "\n";
    var object = null;
    var properties = null;
    function args(a, index) {
        var myargs = new Array();
        for (var i = index; i < a.length; i++) {
            myargs[myargs.length] = a[i]
        }
        return myargs
    };
    function pad(len) {
        var ret = "";
        for (var i = 0; i < len; i++) {
            ret += indentText
        }
        return ret
    };
    function string(o) {
        var level = 1;
        var indentLevel = indent;
        var ret = "";
        if (arguments.length > 1 && typeof(arguments[1]) == "number") {
            level = arguments[1];
            indentLevel = arguments[2];
            if (o == object) {
                return "[original object]"
            }
        } else {
            iterations = 0;
            object = o;
            if (arguments.length > 1) {
                var list = arguments;
                var listIndex = 1;
                if (typeof(arguments[1]) == "object") {
                    list = arguments[1];
                    listIndex = 0
                }
                for (var i = listIndex; i < list.length; i++) {
                    if (properties == null) {
                        properties = new Object()
                    }
                    properties[list[i]] = 1
                }
            }
        }
        if (iterations++>maxIterations) {
            return "[Max Iterations Reached]"
        }
        if (maxDepth != -1 && level > (maxDepth + 1)) {
            return "..."
        }
        if (typeof(o) == "undefined") {
            return "[undefined]"
        }
        if (o == null) {
            return "[null]"
        }
        if (o == window) {
            return "[window]"
        }
        if (o == window.document) {
            return "[document]"
        }
        if (typeof(o) == "function") {
            return "[function]"
        }
        if (typeof(o) == "boolean") {
            return (o) ? "true": "false"
        }
        if (typeof(o) == "string") {
            return "'" + o + "'"
        }
        if (typeof(o) == "number") {
            return o
        }
        if (typeof(o) == "object") {
            if (typeof(o.length) == "number") {
                if (maxDepth != -1 && level > maxDepth) {
                    return "[ ... ]"
                }
                ret = "[";
                for (var i = 0; i < o.length; i++) {
                    if (i > 0) {
                        ret += "," + newline + pad(indentLevel)
                    } else {
                        ret += newline + pad(indentLevel)
                    }
                    ret += string(o[i], level + 1, indentLevel - 0 + indent)
                }
                if (i > 0) {
                    ret += newline + pad(indentLevel - indent)
                }
                ret += "]";
                return ret
            } else {
                if (maxDepth != -1 && level > maxDepth) {
                    return "{ ... }"
                }
                ret = "{";
                var count = 0;
                for (i in o) {
                    if (o == object && properties != null && properties[i] != 1) {} else {
                        if (typeof(o[i]) != "unknown") {
                            var processAttribute = true;
                            if (o.ownerDocument || o.tagName || (o.nodeType && o.nodeName)) {
                                processAttribute = false;
                                if (i == "tagName" || i == "nodeName" || i == "nodeType" || i == "id" || i == "className") {
                                    processAttribute = true
                                }
                            }
                            if (processAttribute) {
                                if (count++>0) {
                                    ret += "," + newline + pad(indentLevel)
                                } else {
                                    ret += newline + pad(indentLevel)
                                }
                                ret += "'" + i + "' => " + string(o[i], level + 1, indentLevel - 0 + i.length + 6 + indent)
                            }
                        }
                    }
                }
                if (count > 0) {
                    ret += newline + pad(indentLevel - indent)
                }
                ret += "}";
                return ret
            }
        }
    };
    string.popup = function(o) {
        var w = window.open("about:blank");
        w.document.open();
        w.document.writeln("<HTML><BODY><PRE>");
        w.document.writeln(string(o, args(arguments, 1)));
        w.document.writeln("</PRE></BODY></HTML>");
        w.document.close()
    };
    string.alert = function(o) {
        alert(string(o, args(arguments, 1)))
    };
    string.write = function(o) {
        var argumentsIndex = 1;
        var d = document;
        if (arguments.length > 1 && arguments[1] == window.document) {
            d = arguments[1];
            argumentsIndex = 2
        }
        var temp = indentText;
        indentText = "&nbsp;";
        d.write(string(o, args(arguments, argumentsIndex)));
        indentText = temp
    };
    string.setMaxIterations = function(i) {
        maxIterations = i
    };
    string.setMaxDepth = function(i) {
        maxDepth = i
    };
    string.$VERSION = 1.0;
    return string
})();