(function() {
    var l = {
        container: "quickQuery_container",
        resultNumBox: "quickQuery_resultNum_box",
        contentBox: "quickQuery_content_box",
        pageBox: "quickQuery_page_box",
        ieContentFrame: "quickQuery_frame_forIE",
        ieContentBox: "quickQuery_content_box_forIE"
    };
    var m = {
        exprElem: "quickQuery$focus"
    };
    var n = document.all ? true : false;
    var o = {
        'isSubVal': false,
        'isPage': true,
        'subSign': "-",
        'buttonNum': 5,
        'viewContentNum':15
    };
    var p = {
        'subval': 0,
        'hz': 1,
        'py': 2
    };
    var q = null;
    var r = 1;
    var s = o['buttonNum'];
    var t = 1;
    var u = [];
    var v = [];
    String.prototype.trim = function() {
        return this.replace(/^\s*|\s*$/g, '')
    };
    var $ = function(a) {
        return document.getElementById(a)
    };
    var w = window.$quickQuery = function(b, c) {
        var d = [],
            e = [],
            f = null,
            g = null,
            h = true,
            j = null;
        if (b) {
            if (c) setConfig(c);
            if (b.length) {
                f = 0;
                //d = getElemArray();
                e = b
            } else {
                f = 1;
                for (var k in b) {
                    if ($(k)) {
                        d.push($(k));
                        e.push(b[k])
                    }
                }
            }
            d.each(function(i) {
                var a = this;
                a.onfocus = a.onclick = function() {
                    h = false;
                    a.value = "";
                    if (a != g) {
                        r = 1;
                        s = o['buttonNum'];
                        t = 1
                    }
                    getQueryPanel(a);
                    callDifferentModel(f, a, e, i);
                    a.focus();
                    g = a
                };
                a.onkeyup = function() {
                    r = 1, s = o['buttonNum'], t = 1;
                    callDifferentModel(f, a, e, i)
                };
                a.onblur = function() {
                    h = true
                };
                document.onclick = function() {
                    clearTimeout(j);
                    j = setTimeout(function() {
                        if (h) closeQueryPanel()
                    }, 10)
                }
            })
        }
    };

    function callDifferentModel(a, b, c, i) {
        if (a == 0) {
            bindDataAndGetHtml(b, c)
        } else if (a == 1) {
            bindDataAndGetHtml(b, c[i])
        }
    };

    function bindDataAndGetHtml(a, b) {
        putDataIntoArray(a.value.trim(), b);
        bindDataIntoElements(function() {
            if (n) {
                $(l.ieContentFrame).style.width = $(l.ieContentBox).offsetWidth + "px";
                $(l.ieContentFrame).style.height = $(l.ieContentBox).offsetHeight + "px"
            }
        })
    };

    function setConfig(a) {
        for (var b in a) {
            o[b] = a[b]
        }
    };
    Array.prototype.each = function(a) {
        for (var i = 0; i < this.length; i++) a.call(this[i], i)
    };

    function getQueryPanel(a) {
        q = a;
        if (!$(l.container)) {
            var b = document.createElement("div");
            b.id = l.container;
            document.body.appendChild(b);
            var c = '';
            var d = '<div id=' + l.contentBox + '></div>';
            var e = '';
            if (!n) {
                b.innerHTML = c + d + e
            } else {
                var f = '<iframe id=' + l.ieContentFrame + ' frameborder="0"></iframe>';
                var g = '<div id=' + l.ieContentBox + '>' + c + d + e + '<div>';
                b.innerHTML = f + g
            }
        } else {
            show($(l.container))
        }
        setPanelStyle()
    };

    function setPanelStyle() {
        var lf=q.getBoundingClientRect().left,tp=q.getBoundingClientRect().top,oh=q.offsetHeight;
        $(l.container).style.left = lf +"px";
        $(l.container).style.top = tp + oh - (n ?1 :2) +3+ "px";
        if (n) {
            $(l.container).style.border = "none"
        }
    };

    function putDataIntoArray(a, b) {
        u = [];
        try {
            var c = RegExp(a, "gi");
            for (var i = 0; i < b.length; i++) {
                var d = c.test(b[i][p['hz']]),
                    f = c.test(b[i][p['py']]);
                if (d || f) {
                    u.push(b[i]);
                }
            }
        } catch (e) {}
    };

    function bindDataIntoElements(a) {
        var b = u.length,
            c = 0,
            d = b;
        var e = t * o['viewContentNum'],
            f = e - o['viewContentNum'] + 1;
        if (!o['isPage']) {
            e = b
        }
        if (o['isPage'] && e >= b) {
            e = u.length
        }
        if (o['isPage']) {
            d = (b < o['viewContentNum']) ? b : o['viewContentNum'];
            if (t != 1) {
                c = (t - 1) * o['viewContentNum'];
                d = c + o['viewContentNum']
            }
            getPageViewContentHtml(c, (c + o['viewContentNum']));
        } else {
            getPageViewContentHtml(c, d)
        }
        if (typeof a == "function") {
            a.call(this)
        }
    };

    function getPageViewContentHtml(a, b) {
        if (u.length < a) {
            a = u.length - o['viewContentNum'];
            b = u.length
        }
        if (a < 0) {
            a = 0;
            b = o['viewContentNum']
        }
        var c = '<ul>';
        for (var i = a; i < b; i++) {
            if (u[i]) {
                var d = u[i][p['subval']],
                    e = u[i][p['hz']];
                if (o['isSubVal']) h = e + o['subSign'] + d;
                c += '<li><a href=javascript:$query_getClickValue("' + e.replace(/\s*/g,"")+ '","' + d+ '");>' + e + '</a></li>';
            } else {
                //c += '<li></li>'
            }
        }
        c += '</ul>';
        if(c=='<ul></ul>'){c='<ul><li>未找到匹配的店铺...</li></ul>'}
        $(l.contentBox).innerHTML = c
    };
    var x = window.$query_getClickValue = function(a,d) {
        q.value = a;
        q.name=d;
        closeQueryPanel();
    };
    var y = window.$query_topage = function(a) {
        t = a;
        q.focus();
        bindDataAndGetHtml(q, u)
    };
    var z = window.$query_prevRound = function() {
        r = r - o['buttonNum'];
        s = s - o['buttonNum'];
        t = r;
        q.focus();
        bindDataAndGetHtml(q, u)
    };
    var A = window.$query_nextRound = function() {
        r = r + o['buttonNum'];
        s = s + o['buttonNum'];
        t = r;
        q.focus();
        bindDataAndGetHtml(q, u)
    };

    function closeQueryPanel(){
        hide($(l.container))
    };

    function show(a) {
        if (a) a.style.display = "block";
    };

    function hide(a) {
        if (a) a.style.display = "none"
    };
})();
