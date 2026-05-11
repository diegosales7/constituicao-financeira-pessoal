async function login() {
  const email = document.getElementById("email").value;
  const senha = document.getElementById("senha").value;

  try {
    const res = await fetch("http://localhost:10000/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password: senha })
    });

    const data = await res.json();

    if(res.ok){
      localStorage.setItem("token", data.token);  // salva token no navegador
      alert("Login feito!");
      window.location.href = "perfil.html";       // vai para o dashboard
    } else {
      alert("Erro: " + data.message);
    }
  } catch(err) {
    console.error(err);
    alert("Erro na requisição");
  }
}