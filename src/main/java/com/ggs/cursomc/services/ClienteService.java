package com.ggs.cursomc.services;

import java.net.URI;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.ggs.cursomc.repositories.DBRepository;
import com.ggs.cursomc.security.UserSS;
import com.ggs.cursomc.services.exceptions.AuthorizationException;

@Service
public class ClienteService extends AppService<Cliente> {
	
	@Autowired S3Service s3Service;
	@Autowired BCryptPasswordEncoder bc;

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
		if(isFindOk(id))
			return super.find(id);
		throw new AuthorizationException("Acesso negado");
	}

	private static boolean isFindOk(Integer id) {
		return isUserAuthenticated() && isUserPermited(id);
	}

	private static boolean isUserAuthenticated() {
		return user() != null;
	}
	
	private static UserSS user() {
		return UserService.authenticated();
	}

	private static boolean isUserPermited(Integer id) {
		return isSelf(id) || isUserAdmin();
	}

	private static boolean isSelf(Integer id) {
		return user().getId() == id;
	}

	private static boolean isUserAdmin() {
		return user().hasRole(Perfil.ADMIN);
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		if(!isUserAuthenticated())
			throw new AuthorizationException("Acesso negado");
		
		URI uri = s3Service.uploadFile(multipartFile);
		updateCliente(uri);
		return uri;
	}

	private void updateCliente(URI uri) {
		Cliente cliente = DBRepository.findOne(Cliente.class, user().getId());
		cliente.setImageUrl(uri.toString());
		DBRepository.save(cliente);
	}

}