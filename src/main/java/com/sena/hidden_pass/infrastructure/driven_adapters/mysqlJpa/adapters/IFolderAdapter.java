package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.domain.models.FolderModel;
import com.sena.hidden_pass.domain.usecases.FolderUseCases;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.FolderDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PasswordDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IFolderRepository;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IPasswordRepository;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IUserRepository;
import com.sena.hidden_pass.infrastructure.mappers.FolderMapper;
import com.sena.hidden_pass.infrastructure.mappers.UserMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class IFolderAdapter implements FolderUseCases {

    private IFolderRepository folderRepository;
    private IPasswordRepository passwordRepository;
    private IUserRepository userRepository;

    public IFolderAdapter(IUserRepository userRepository, IPasswordRepository passwordRepository, IFolderRepository folderRepository) {
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
        this.folderRepository = folderRepository;
    }

    @Override
    public List<FolderModel> getAllFolders(UUID userId) {
        UserDBO userFounded = this.userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userFounded.getFolderList().stream().map(FolderMapper::folderDBOToModel).toList();
    }

    @Override
    public FolderModel getFolderById(UUID id) {
        FolderDBO folderFounded = folderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Folder with id " + id + " not found"));
        return FolderMapper.folderDBOToModel(folderFounded);
    }

    @Override
    public FolderModel getFolderByName(String name) {
        FolderDBO folderFounded = folderRepository.findFolderDBOByName(name).orElseThrow(() -> new IllegalArgumentException("Folder with name " + name + " not found"));
        return FolderMapper.folderDBOToModel(folderFounded);
    }

    @Override
    public FolderModel createFolder(FolderModel folder, UUID userId, List<String> passwordsNames) {

        UserDBO userFounded = this.userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        folder.setUser(UserMapper.userDBOToModel(userFounded));


        FolderDBO dbo = FolderMapper.folderModelToDBO(folder);
        if(passwordsNames != null){
            List<PasswordDBO> passwords = this.passwordRepository.findPasswordDBOByNameIn(passwordsNames);
            dbo.setPasswords(passwords);
        }
        dbo.setUser(userFounded);
        FolderDBO folderSaved = folderRepository.save(dbo);

        userFounded.getFolderList().add(folderSaved);
        userRepository.save(userFounded);

        return FolderMapper.folderDBOToModel(folderSaved);
    }

    @Override
    public FolderModel updateFolder(FolderModel folder, UUID id, List<String> passwordsNames) {
        FolderDBO folderFounded = folderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Folder with id " + id + " not found"));

        folderFounded.setName(folder.getName());
        folderFounded.setDescription(folder.getDescription());
        folderFounded.setIcon(folder.getIcon());
        if(passwordsNames != null){
            List<PasswordDBO> passwords = this.passwordRepository.findPasswordDBOByNameIn(passwordsNames);
            folderFounded.setPasswords(passwords); // Actualiza el estado
        }else{
            folderFounded.setPasswords(new ArrayList<>());
        }
        folderFounded.setUser(folderFounded.getUser()); // -> No actualiza el estado

        FolderDBO folderSaved = folderRepository.save(folderFounded);
        return FolderMapper.folderDBOToModel(folderSaved);
    }

    @Override
    public FolderModel setPasswordToFolder(UUID folderId, UUID passwordId) {
        FolderDBO folderFounded = this.folderRepository.findById(folderId).orElseThrow(() -> new IllegalArgumentException("Folder with id " + folderId + " not found"));

        PasswordDBO passwordFounded = this.passwordRepository.findById(passwordId).orElseThrow(() -> new IllegalArgumentException("Password with id " + passwordId + " not found"));

        folderFounded.getPasswords().add(passwordFounded);

        FolderDBO folderSaved = this.folderRepository.save(folderFounded);
        passwordFounded.setId_folder(folderSaved);

        passwordRepository.save(passwordFounded);

        return FolderMapper.folderDBOToModel(folderSaved);
    }

    @Override
    public String deleteFolder(UUID id) {
        FolderDBO folderFounded = folderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Folder with id " + id + " not found"));

        List<PasswordDBO> passwords = folderFounded.getPasswords().stream()
                .map(passwordDBO -> {
                    passwordDBO.setId_folder(null);
                    return passwordDBO;
                }).toList();

        this.passwordRepository.saveAll(passwords);

        this.folderRepository.delete(folderFounded);

        return "Folder with id " + id + " deleted successfully";
    }
}
