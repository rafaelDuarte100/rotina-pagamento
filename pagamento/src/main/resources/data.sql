INSERT INTO operation_type(operation_type_id, description, charge_order) VALUES (1, 'COMPRA À VISTA', 2) ON CONFLICT (operation_type_id) DO NOTHING;
INSERT INTO operation_type(operation_type_id, description, charge_order) VALUES (2, 'COMPRA PARCELADA', 1) ON CONFLICT (operation_type_id) DO NOTHING;
INSERT INTO operation_type(operation_type_id, description, charge_order) VALUES (3, 'SAQUE', 0) ON CONFLICT (operation_type_id) DO NOTHING;
INSERT INTO operation_type(operation_type_id, description, charge_order) VALUES (4, 'PAGAMENTO', 0) ON CONFLICT (operation_type_id) DO NOTHING;