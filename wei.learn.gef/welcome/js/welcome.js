/**
 * Created by Administrator on 2016/10/12 0012.
 */
function configContent(refName) {
    var frameObj = document.getElementById(refName + "_frame");
    var newDocument = frameObj.contentWindow.document;
    var frameContent = newDocument.getElementById("mainContent").innerHTML;
    document.getElementById("mainContent").innerHTML = frameContent;
}
// ≥ı ºªØ
window.onload = function () {
    configContent("summary");
}
;

