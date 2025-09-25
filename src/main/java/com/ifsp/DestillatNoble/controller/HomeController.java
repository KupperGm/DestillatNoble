package com.ifsp.DestillatNoble.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ifsp.DestillatNoble.dao.AdminDao;
import com.ifsp.DestillatNoble.dao.BebidaDao;
import com.ifsp.DestillatNoble.dao.FuncionarioDao;
import com.ifsp.DestillatNoble.dao.UsuarioDao;
import com.ifsp.DestillatNoble.model.Admin;
import com.ifsp.DestillatNoble.model.Bebida;
import com.ifsp.DestillatNoble.model.Funcionario;
import com.ifsp.DestillatNoble.model.Usuario;
import com.ifsp.DestillatNoble.service.AdminService;
import com.ifsp.DestillatNoble.service.FuncionarioService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class HomeController {
    /*COOKIES  */


    @Value("${app.cookies.same-site:Lax}")
    private String deafultSameSite;

    @Value("${app.cookies.secure:false}")
    private boolean deafultSecure;

    @PostMapping("/accept")
    @ResponseBody
    public ResponseEntity<Void> acceptCookies(HttpServletResponse response){
        
        long oneYearSeconds = 365L * 24 * 60 * 60;

       setCookie(response, "cookie_consent", "true", oneYearSeconds, deafultSecure, deafultSameSite, true);

        return ResponseEntity.ok().build();
    }



    // ==== Utilidades de Cookie ====

    private void setCookie(HttpServletResponse resp, String name, String value, long maxAgeSeconds, boolean secure, String sameSite, boolean httpOnly){
        ResponseCookie cookie = ResponseCookie.from(name, value)
            .httpOnly(httpOnly)
            .secure(secure)
            .sameSite(sameSite)
            .path("/")
            .maxAge(Duration.ofSeconds(maxAgeSeconds))
            .build();
            resp.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
    private void deleteCookie(HttpServletResponse resp, String name){
        ResponseCookie cookie = ResponseCookie.from(name, "")
        .path("/")
        .maxAge(Duration.ZERO)
        .build();
        resp.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }


    private Map<String, String> readCookies(HttpServletRequest req){
        Map<String, String> map = new LinkedHashMap<>();
        Cookie [] cookies = req.getCookies();
        if (cookies != null) {
            for(Cookie ck: cookies){
                map.put(ck.getName(), ck.getValue());
            }
        }
        return map;
    }

    /*FIM  DE COOKIES */

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private FuncionarioDao funcDao;

    @Autowired
    private AdminDao adminDao;
    
    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private BebidaDao bebidaDao;

    @GetMapping("/home")
    public String home(Model model) {
        List<Bebida> bebidas = bebidaDao.findAll();
        model.addAttribute("bebidas", bebidas);
        return "index.html";
    }
    @GetMapping("/loginform")
    public String login() {
        return "login.html";
    }
    @PostMapping("/login")
    public String loginPost(@RequestParam String tipo, @RequestParam String senha, @RequestParam String email) {
        if (tipo.equals("admin") || tipo.equals("funcionario")) {
        return "redirect:/FormFun";
        }
        return "redirect:/home";
    }
    @GetMapping("/FormFun")
    public String formFun() {
        return "LoginFun.html";
    }
    @PostMapping("/loginFun")
    public String loginFun() {
        return "redirect:/home";
    }
    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model){
        Bebida bebida = bebidaDao.findById(id).orElse(null);
        model.addAttribute("bebida", bebida);
        return "detalhes";
    }
    /* -- Terminar Registro geral para poder fazer os registros de adm e func --*/
    @GetMapping("/registerForm")
    public String register(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register";
    }
    @PostMapping("/registerUsuario")
    public String registerUser(@Valid Usuario usuario, BindingResult result, Model model) {
        
        if (result.hasErrors()) {
            return "register.html"; 
        }
        if (usuario.getNascimento() != null) {
            LocalDate dataNasc = usuario.getNascimento();
            int idade = Period.between(dataNasc, LocalDate.now()).getYears();
                if (idade < 18) {
                    result.rejectValue("nascimento", "error.usuario", "Você deve ser maior de 18 anos para se registrar!");
                    return "register.html";
                }
        }
              if (usuarioDao.findByCpf(usuario.getCpf()).isPresent() && usuarioDao.findByEmail(usuario.getEmail()).isPresent()) {
            result.rejectValue("cpf", "error.usuario", "CPF e Email já cadastrados em nosso sistema!");
            result.rejectValue("email", "error.usuario", "CPF e Email já cadastrados em nosso sistema!");
            return "register.html";
            
        }
        if (usuarioDao.findByCpf(usuario.getCpf()).isPresent()) {
            result.rejectValue("cpf", "error.usuario", "CPF já cadastrado em nosso sistema!");
            return "register.html";
        }
        if (usuarioDao.findByEmail(usuario.getEmail()).isPresent()) {
            result.rejectValue("email", "error.usuario", "Email já cadastrado em nosso sistema!");
            return "register.html";
            
        }
        
        usuarioDao.save(usuario);
        return "redirect:/loginform"; 
    }


    /* -- Fim de registro geral e inicio de testes -- */


    /*TESTE DE CADASTRO DE ADM E FUNCIONÁRIOS NO DATABASE */
    @GetMapping("/adminCad")
    public String adminCad(Model model) {
        model.addAttribute("admin", new Admin());
        return "adminCad.html";
    }
    @PostMapping("/registerAdm")
    public String registerAdmin(@Valid Admin admin, BindingResult result, Model model) {
        
        if (result.hasErrors()) {
        model.addAttribute("admin", admin);
        return "adminCad.html";  // aqui também se 'adminCad.html' usa o objeto, adiciona no model
    }

    if (adminDao.findByCpf(admin.getCpf()).isPresent() && adminDao.findByEmail(admin.getEmail()).isPresent()) {
        result.rejectValue("cpf", "error.admin", "CPF e Email já cadastrados em nosso sistema!");
        result.rejectValue("email", "error.admin", "CPF e Email já cadastrados em nosso sistema!");
        model.addAttribute("admin", admin);
        return "adminCad.html";
    }

    if (adminDao.findByCpf(admin.getCpf()).isPresent()) {
        result.rejectValue("cpf", "error.admin", "CPF já cadastrado em nosso sistema!");
        model.addAttribute("admin", admin);
        return "adminCad.html";
    }

    if (adminDao.findByEmail(admin.getEmail()).isPresent()) {
        result.rejectValue("email", "error.admin", "Email já cadastrado em nosso sistema!");
        model.addAttribute("admin", admin);
        return "adminCad.html";
    }
        
        adminDao.save(admin);
        return "redirect:/indexAdmin";
    }

    @GetMapping("/funcCad")
    public String funcCad(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        return "funCad.html";
    }
    @PostMapping("/registerFunc")
    public String registerFunc(@Valid Funcionario funcionario, BindingResult result, Model model) {
         if (result.hasErrors()) {
        model.addAttribute("funcionario", funcionario);
        return "funCad.html";  // aqui também se 'funCad.html' usa o objeto, adiciona no model
    }

    if (funcDao.findByCpf(funcionario.getCpf()).isPresent() && funcDao.findByEmail(funcionario.getEmail()).isPresent()) {
        result.rejectValue("cpf", "error.funcionario", "CPF e Email já cadastrados em nosso sistema!");
        result.rejectValue("email", "error.funcionario", "CPF e Email já cadastrados em nosso sistema!");
        model.addAttribute("funcionario", funcionario);
        return "funCad.html";
    }

    if (funcDao.findByCpf(funcionario.getCpf()).isPresent()) {
        result.rejectValue("cpf", "error.funcionario", "CPF já cadastrado em nosso sistema!");
        model.addAttribute("funcionario", funcionario);
        return "funCad.html";
    }

    if (funcDao.findByEmail(funcionario.getEmail()).isPresent()) {
        result.rejectValue("email", "error.funcionario", "Email já cadastrado em nosso sistema!");
        model.addAttribute("funcionario", funcionario);
        return "funCad.html";
    }

        
        
        funcDao.save(funcionario);
        return "redirect:/indexAdmin";
    }
    /*  FIM DE TESTE */

    /*INCIO DE TESTE MONITORAMENTO REALIZADO PELOS ADMINS*/

    @GetMapping("/indexAdmin")
    public String indexAdmin() {
        return "IndexAdmin.html";
    }

    @GetMapping("/listarClientes")
    public String listarClientes(Model model) {
        List<Usuario> clientes = usuarioDao.findAll();
        model.addAttribute("clientes", clientes);
        return "listar-clientes.html";
    }
    @GetMapping("/listarAdmins")
    public String listarAdmins(Model model) {
 
        model.addAttribute("admins", adminService.listarAtivos());
        return "listar-admins.html";
    }
    @GetMapping("/editarAdm/{matricula}")
    public String editarAdm (@PathVariable String matricula, Model model) {
        Admin admin = adminService.buscarPorMatricula(matricula);
        model.addAttribute("admin", admin);
        
        return "admEdit.html";

    }
    @PostMapping("/salvarAdm")
    public String salvarAdm(@Valid Admin admin, BindingResult result, Model model){

        adminService.salvar(admin);
        return "redirect:/listarAdmins";

    }
    @PostMapping("/desativarAdm/{matricula}")
    public String desativarAdm(@PathVariable String matricula){
        adminService.desativar(matricula);
        return "redirect:/listarAdmins";
    }
    @GetMapping("/listarAdmOff")
    public String listarAdmDesativados(Model model){
        model.addAttribute("admins", adminService.listarInativos());
        return "listar-adminsOff.html";
    }   

    @GetMapping("/listarFuncionarios")
    public String listarFuncionarios(Model model) {
        
        model.addAttribute("funcionarios", funcionarioService.listarAtivos());

        return "listar-funcionarios.html";
    }
    @PostMapping("/salvarFuncionario")
    public String salvarFuncionario(@Valid Funcionario funcionario, BindingResult result, Model model){
        funcionarioService.salvar(funcionario);
        return "redirect:/listarFuncionarios";
    }

     @GetMapping("/editar/{matricula}")
    public String editarFuncionario(@PathVariable String matricula, Model model) {
        Funcionario funcionario = funcionarioService.buscarPorMatricula(matricula);
        model.addAttribute("funcionario", funcionario);
        
        return "funEdit.html";
        
    }
     @PostMapping("/desativar/{matricula}")
    public String desativarFuncionario(@PathVariable String matricula){
        funcionarioService.desativar(matricula);
        return "redirect:/listarFuncionarios";
    }
    @GetMapping("/listarFuncionariosOff")
    public String listarDesativados(Model model){
        model.addAttribute("funcionarios", funcionarioService.listarInativos());
        return "listar-funcionariosOff.html";
    }   

    /*FIM DE TESTES */
    /*INCIO DO PAINEL DO FUNCIONÁRIO */
    @GetMapping("/indexFun")
    public String indexFun() {
        return "indexFun.html";
    }
    @GetMapping("/addBebida")
    public String addBebida() {
        return "addBebidas.html";
    }
    @PostMapping("/bebidasAdd")
    public String bebidasAdd(@RequestParam("nome") String nome,
                             @RequestParam("categoria") String categoria,
                             @RequestParam("descricao") String descricao,
                             @RequestParam("quantidade") Integer quantidade,
                             @RequestParam("preco") Double preco,
                             @RequestParam("imagem") MultipartFile imagem) throws IOException {
        
        Bebida bebida = new Bebida();
        bebida.setNome(nome);
        bebida.setCategoria(categoria);
        bebida.setDescricao(descricao);
        bebida.setPreco(preco);
        bebida.setQuantidade(quantidade);
        bebida.setImagem(imagem.getBytes());
        
        bebidaDao.save(bebida);
           
       return "redirect:/indexFun";
    }
    @GetMapping("/listarBebidas")
    public String listarBebidas(Model model) {
        
        List<Bebida> bebidas = bebidaDao.findAll();
        model.addAttribute("bebidas", bebidas);
        return "listar-bebidas.html";
    }
    @GetMapping("/bebidaImagem/{id}")
    public ResponseEntity<byte[]> exibirImagem(@PathVariable Long id) {
    Bebida bebida = bebidaDao.findById(id).orElse(null);

    if (bebida == null || bebida.getImagem() == null) {
        return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG) // ou IMAGE_PNG, dependendo do caso
            .body(bebida.getImagem());
}

    @PostMapping("/salvarBebida")
    public String salvarBebida(@Valid Bebida bebida, BindingResult result, @RequestParam("imagem") MultipartFile imagem, Model model) throws IOException{
        bebida.setImagem(imagem.getBytes());
        bebidaDao.save(bebida);
        return "redirect:/listarBebidas";
    }

    @PostMapping("/excluirBebida/{id}")
    public String desativarBebida(@PathVariable Long id){
        bebidaDao.deleteById(id);
        return "redirect:/listarBebidas";
    }
    @GetMapping("/editarBebida/{id}")
    public String editarBebida(@PathVariable Long id, Model model) {
        Bebida bebida = bebidaDao.findById(id).orElse(null);
        model.addAttribute("bebida", bebida);
        
        return "bebidaEdit.html";

    }

}


