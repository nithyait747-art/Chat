<%@ page contentType="text/html;charset=UTF-8" %>
<%
String currentUser=(String)session.getAttribute("username");
if(currentUser==null){response.sendRedirect("login.jsp");return;}
%>
<!DOCTYPE html>
<html><head><meta charset="UTF-8"><title>Chat</title>
<style>
body{font-family:Arial;background:#eef2f7;margin:0}.header{background:#2563eb;color:#fff;padding:15px 22px;display:flex;justify-content:space-between}
.container{max-width:850px;margin:25px auto;background:#fff;border-radius:12px;overflow:hidden;box-shadow:0 4px 18px #ccc}
.top{padding:18px;border-bottom:1px solid #ddd}.top input{padding:10px;width:220px;border:1px solid #ccc;border-radius:6px}
.messages{height:430px;overflow-y:auto;padding:18px;background:#f8fafc}.msg{padding:10px 12px;margin:8px 0;border-radius:9px;max-width:70%;background:#e5e7eb}
.mine{margin-left:auto;background:#dbeafe}.meta{font-size:11px;color:#666}.text{font-size:15px;word-wrap:break-word}
.send{display:flex;gap:8px;padding:15px;border-top:1px solid #ddd}.send input{flex:1;padding:12px;border:1px solid #ccc;border-radius:7px}
.send button{padding:12px 20px;border:0;border-radius:7px;background:#2563eb;color:#fff}.logout{color:#fff}
</style></head><body>
<div class="header"><b>Simple Chatting System</b><span>Logged in: <%=currentUser%> | <a class="logout" href="logout">Logout</a></span></div>
<div class="container"><div class="top"><b>Chat with:</b>
<input id="receiver" placeholder="Enter receiver username" oninput="loadMessages()"></div>
<div class="messages" id="messages"></div>
<div class="send"><input id="message" placeholder="Type your message..." maxlength="1000"
onkeydown="if(event.key==='Enter')sendMessage()"><button onclick="sendMessage()">Send</button></div></div>
<script>
const currentUser="<%=currentUser%>";
function loadMessages(){
 const receiver=document.getElementById("receiver").value.trim();
 if(!receiver){document.getElementById("messages").innerHTML="";return;}
 fetch("messages?receiver="+encodeURIComponent(receiver))
 .then(r=>r.json()).then(data=>{
  const box=document.getElementById("messages");box.innerHTML="";
  data.forEach(m=>{
   const d=document.createElement("div");
   d.className="msg "+(m.sender===currentUser?"mine":"");
   d.innerHTML="<div class='meta'>"+esc(m.sender)+" → "+esc(m.receiver)+" | "+esc(m.time)+"</div><div class='text'>"+esc(m.message)+"</div>";
   box.appendChild(d);
  });
  box.scrollTop=box.scrollHeight;
 }).catch(()=>{});
}
function sendMessage(){
 const receiver=document.getElementById("receiver").value.trim();
 const message=document.getElementById("message").value.trim();
 if(!receiver||!message){alert("Enter receiver and message");return;}
 fetch("sendMessage",{method:"POST",headers:{"Content-Type":"application/x-www-form-urlencoded"},
 body:"receiver="+encodeURIComponent(receiver)+"&message="+encodeURIComponent(message)})
 .then(r=>r.text()).then(result=>{
  if(result==="success"){document.getElementById("message").value="";loadMessages();}
  else alert(result);
 });
}
function esc(s){return String(s).replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#039;'}[c]));}
setInterval(loadMessages,3000);
</script></body></html>
