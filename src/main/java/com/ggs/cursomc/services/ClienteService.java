package com.ggs.cursomc.services;

import static com.ggs.cursomc.repositories.DBRepository.repository;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URI;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ggs.cursomc.domain.Cidade;
import com.ggs.cursomc.domain.Cliente;
import com.ggs.cursomc.domain.Endereco;
import com.ggs.cursomc.domain.enums.Perfil;
import com.ggs.cursomc.domain.enums.TipoCliente;
import com.ggs.cursomc.dto.ClienteDTO;
import com.ggs.cursomc.dto.ClienteNewDTO;
import com.ggs.cursomc.repositories.ClienteRepository;
import com.ggs.cursomc.repositories.DBRepository;
import com.ggs.cursomc.security.UserSS;
import com.ggs.cursomc.services.exceptions.AuthorizationException;

@Service
public class ClienteService extends AppService<Cliente> {
	
	@Autowired S3Service s3Service;
	@Autowired BCryptPasswordEncoder bc;
	@Autowired ImageService imageService;

	@Value("${img.prefix.client.profile}")
	private String prefix;

	@Value("${img.profile.size}")
	private Integer size;

	@Override
	protected void updateData(Cliente oldObj, Cliente newObj) {
		oldObj.setNome(newObj.getNome());
		oldObj.setEmail(newObj.getEmail());
	}

	public Cliente fromDTO(ClienteDTO c) {
		Cliente cliente = new Cliente();
		cliente.setId(c.getId());
		cliente.setNome(c.getNome());
		cliente.setEmail(c.getEmail());
		return cliente;
	}

	public Cliente fromDTO(ClienteNewDTO dto) {
		Cliente obj = new Cliente();
		obj.setCpfOuCnpj(dto.getCpfOuCnpj());
		obj.setEmail(dto.getEmail());
		obj.setNome(dto.getNome());
		obj.setTelefones(new HashSet<String>(dto.getTelefones()));
		obj.setTipo(TipoCliente.toEnum(dto.getTipo()));
		obj.setSenha(bc.encode(dto.getSenha()));
		
		Cidade cidade = new Cidade();
		cidade.setId(dto.getCidadeId());
		
		Endereco end = new Endereco();
		end.setBairro(dto.getBairro());
		end.setCep(dto.getCep());
		end.setComplemento(dto.getComplemento());
		end.setLogradouro(dto.getLogradouro());
		end.setNumero(dto.getNumero());
		
		end.setCidade(cidade);
		
		end.setCliente(obj);
		
		obj.getEnderecos().add(end);
		
		return obj; 
	}

	@Transactional
	@Override
	public Cliente insert(Cliente c) {
		c.setId(null);
		DBRepository.save(c);
		DBRepository.save(c.getEnderecos());
		return c;
	}
	
	@Override
	public Cliente find(Integer id) {
		verifyPermition(id);
		return super.find(id);
	}

	private void verifyPermition(Integer id) {
		if(!isAllowed(id))
			throw new AuthorizationException("Acesso negado");
	}

	private static boolean isAllowed(Integer id) {
		return isUserAuthenticated() && isUserAllowed(id);
	}

	private static boolean isUserAuthenticated() {
		return user() != null;
	}
	
	private static UserSS user() {
		return UserService.authenticated();
	}

	private static boolean isUserAllowed(Integer id) {
		return isSelf(id) || isUserAdmin();
	}

	private static boolean isSelf(Integer id) {
		return user().getId() == id;
	}

	private static boolean isUserAdmin() {
		return user().hasRole(Perfil.ADMIN);
	}
	
	public URI uploadProfilePicture(MultipartFile file) {
		if(!isUserAuthenticated())
			throw new AuthorizationException("Acesso negado");
		
		return s3Service.uploadFile(jpgInputStream(jpgImage(file)), imageName(), "image");
	}

	private BufferedImage jpgImage(MultipartFile multipartFile) {
		BufferedImage img = imageService.cropSquare(imageService.getJpgImageFromFile(multipartFile));
		return imageService.resize(img, size);
	}

	private InputStream jpgInputStream(BufferedImage jpgImage) {
		return imageService.getInputStream(jpgImage, "jpg");
	}

	private String imageName() {
		return prefix + user().getId() + ".jpg";
	}

	public Cliente findByEmail(String email) {
		verifyPermition(email);
		return repository(ClienteRepository.class).findByEmail(email);
	}

	private static void verifyPermition(String email) {
		if(!isAllowed(email))
			throw new AuthorizationException("Acesso negado");
	}

	private static boolean isAllowed(String email) {
		return isUserAuthenticated() && isUserAllowed(email);
	}

	private static boolean isUserAllowed(String email) {
		return isSelf(email) || isUserAdmin();
	}

	private static boolean isSelf(String email) {
		return user().getUsername().equals(email);
	}

}