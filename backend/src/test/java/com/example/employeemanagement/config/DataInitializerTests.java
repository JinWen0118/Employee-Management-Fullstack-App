package com.example.employeemanagement.config;

import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class DataInitializerTest {

    @Test
    void testRun_WhenDataExists_ShouldSkipInitialization() {
        DepartmentRepository deptRepo = mock(DepartmentRepository.class);
        EmployeeRepository empRepo = mock(EmployeeRepository.class);

        when(deptRepo.count()).thenReturn(10L);

        DataInitializer initializer = new DataInitializer();

        // inject mocks manually
        inject(initializer, deptRepo, empRepo);

        initializer.run();

        verify(deptRepo, times(1)).count();
        verifyNoMoreInteractions(deptRepo);
        verifyNoInteractions(empRepo);
    }

    @Test
    void testRun_WhenDatabaseEmpty_ShouldInitializeData() {
        DepartmentRepository deptRepo = mock(DepartmentRepository.class);
        EmployeeRepository empRepo = mock(EmployeeRepository.class);

        when(deptRepo.count()).thenReturn(0L);

        DataInitializer initializer = new DataInitializer();

        inject(initializer, deptRepo, empRepo);

        initializer.run();

        verify(deptRepo).saveAll(any());
        verify(empRepo).saveAll(any());
    }

    // helper injection method
    private void inject(DataInitializer initializer,
                        DepartmentRepository deptRepo,
                        EmployeeRepository empRepo) {
        try {
            var deptField = DataInitializer.class.getDeclaredField("departmentRepository");
            deptField.setAccessible(true);
            deptField.set(initializer, deptRepo);

            var empField = DataInitializer.class.getDeclaredField("employeeRepository");
            empField.setAccessible(true);
            empField.set(initializer, empRepo);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}